package com.abhi.grocery.main.home.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhi.grocery.main.home.domain.PricingUnit
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.main.home.presentation.screen.StoreItemType
import com.abhi.grocery.main.uicomponents.StoreItemTypeSelectorView
import com.abhi.grocery.ui.theme.Geist
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.sampleItemsList
import java.io.File
import java.util.UUID

@Composable
fun AddItemToInventoryView(
    isVisible: MutableState<Boolean>,
    initialType: StoreItemType,
    onAdd: (ProductItem) -> Unit // call this when submitting
) {
    val context = LocalContext.current

    var typeName by rememberSaveable { mutableStateOf(initialType.name) }
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var pricingUnitName by rememberSaveable { mutableStateOf(PricingUnit.PER_KG.name) }

    // kept across the camera app, otherwise a killed activity comes back with a blank photo
    var capturedImageFileName by rememberSaveable { mutableStateOf("") }
    var pendingImageFileName by rememberSaveable { mutableStateOf("") }
    var hasCapturedImage by rememberSaveable { mutableStateOf(false) }

    val type = StoreItemType.valueOf(typeName)
    val pricingUnit = PricingUnit.valueOf(pricingUnitName)
    val capturedImageFile = if(hasCapturedImage) {
        productImageFile(context, capturedImageFileName)?.takeIf { it.exists() && it.length() > 0L }
    } else {
        null
    }
    val canAdd = name.isNotBlank() && price.toDoubleOrNull() != null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val pendingFile = productImageFile(context, pendingImageFileName)
        if(success && pendingFile != null && pendingFile.exists() && pendingFile.length() > 0L) {
            if(capturedImageFileName.isNotBlank() && capturedImageFileName != pendingImageFileName) {
                productImageFile(context, capturedImageFileName)?.delete()
            }
            capturedImageFileName = pendingImageFileName
            hasCapturedImage = true
        } else {
            // cancelled or the camera wrote nothing — don't keep an empty file
            pendingFile?.delete()
        }
        pendingImageFileName = ""
    }

    fun close(deleteCapture: Boolean) {
        productImageFile(context, pendingImageFileName)?.delete()
        if(deleteCapture) {
            productImageFile(context, capturedImageFileName)?.delete()
        }
        isVisible.value = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .imePadding()
            .clickable { close(deleteCapture = true) }
            .padding(horizontal = 45.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White
                )
                .clip(shape = RoundedCornerShape(30.dp))
                .clickable {}
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StoreItemTypeSelectorView(
                selectedItem = type,
                onTypeChange = { typeName = it.name }
            )

            Spacer(Modifier.height(30.dp))

            ImageBox(
                imageFile = capturedImageFile
            ) {
                val file = createImageFile(context)
                pendingImageFileName = file.name
                try {
                    cameraLauncher.launch(cameraOutputUri(context, file))
                } catch(_: ActivityNotFoundException) {
                    file.delete()
                    pendingImageFileName = ""
                }
            }

            Spacer(Modifier.height(8.dp))

            TextFields(
                type = type,
                name = name,
                onNameChange = { name = it },
                price = price,
                onPriceChange = { price = it },
                pricingUnit = pricingUnit,
                onPricingUnitChange = { pricingUnitName = it.name }
            )

            Spacer(Modifier.height(30.dp))

            AddItemButton(
                label = "Add Item",
                enabled = canAdd
            ) {
                val parsedPrice = price.toDoubleOrNull()
                if(name.isBlank() || parsedPrice == null) return@AddItemButton

                onAdd(
                    ProductItem(
                        id = UUID.randomUUID().toString(),
                        name = name.trim(),
                        hindiName = name.trim(),
                        imageName = if(capturedImageFile != null) capturedImageFileName else "",
                        price = parsedPrice,
                        pricingUnit = pricingUnit,
                        productType = type
                    )
                )

                close(deleteCapture = false)
            }
        }
    }
}

@Composable
fun EditItemInInventoryView(
    item: ProductItem,
    onSave: (ProductItem) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var typeName by rememberSaveable(item.id) { mutableStateOf(item.productType.name) }
    var name by rememberSaveable(item.id) { mutableStateOf(item.name) }
    var price by rememberSaveable(item.id) { mutableStateOf(priceFieldText(item.price)) }
    var pricingUnitName by rememberSaveable(item.id) { mutableStateOf(item.pricingUnit.name) }

    var imageFileName by rememberSaveable(item.id) { mutableStateOf(item.imageName) }
    var pendingImageFileName by rememberSaveable(item.id) { mutableStateOf("") }

    val type = StoreItemType.valueOf(typeName)
    val pricingUnit = PricingUnit.valueOf(pricingUnitName)
    val previewFile = productImageFile(context, imageFileName)
        ?.takeIf { it.exists() && it.length() > 0L }
    val canSave = name.isNotBlank() && price.toDoubleOrNull() != null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val pendingFile = productImageFile(context, pendingImageFileName)
        if(success && pendingFile != null && pendingFile.exists() && pendingFile.length() > 0L) {
            // drop the replacement photo, and leave the original until Save
            if(imageFileName.isNotBlank() && imageFileName != item.imageName && imageFileName != pendingImageFileName) {
                productImageFile(context, imageFileName)?.delete()
            }
            imageFileName = pendingImageFileName
        } else {
            pendingFile?.delete()
        }
        pendingImageFileName = ""
    }

    fun close(discardNewPhoto: Boolean) {
        productImageFile(context, pendingImageFileName)?.delete()
        if(discardNewPhoto && imageFileName != item.imageName) {
            productImageFile(context, imageFileName)?.delete()
        }
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .imePadding()
            .clickable { close(discardNewPhoto = true) }
            .padding(horizontal = 45.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White
                )
                .clip(shape = RoundedCornerShape(30.dp))
                .clickable {}
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StoreItemTypeSelectorView(
                selectedItem = type,
                onTypeChange = { typeName = it.name }
            )

            Spacer(Modifier.height(30.dp))

            ImageBox(
                imageFile = previewFile
            ) {
                val file = createImageFile(context)
                pendingImageFileName = file.name
                try {
                    cameraLauncher.launch(cameraOutputUri(context, file))
                } catch(_: ActivityNotFoundException) {
                    file.delete()
                    pendingImageFileName = ""
                }
            }

            Spacer(Modifier.height(8.dp))

            TextFields(
                type = type,
                name = name,
                onNameChange = { name = it },
                price = price,
                onPriceChange = { price = it },
                pricingUnit = pricingUnit,
                onPricingUnitChange = { pricingUnitName = it.name }
            )

            Spacer(Modifier.height(30.dp))

            AddItemButton(
                label = "Save",
                enabled = canSave
            ) {
                val parsedPrice = price.toDoubleOrNull()
                if(name.isBlank() || parsedPrice == null) return@AddItemButton

                if(imageFileName != item.imageName) {
                    productImageFile(context, item.imageName)?.delete()
                }

                onSave(
                    item.copy(
                        name = name.trim(),
                        hindiName = if(item.hindiName == item.name) name.trim() else item.hindiName,
                        imageName = imageFileName,
                        price = parsedPrice,
                        pricingUnit = pricingUnit,
                        productType = type
                    )
                )

                close(discardNewPhoto = false)
            }
        }
    }
}

@Composable
private fun TextFields(
    type: StoreItemType,
    name: String,
    onNameChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    pricingUnit: PricingUnit,
    onPricingUnitChange: (PricingUnit) -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                if(name.isEmpty()) {
                    Text(
                        text = "Enter name of " + type.name,
                        fontFamily = Geist,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.4.sp,
                        color = Color(0xFFD9D9D9),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            HorizontalDivider()
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .height(30.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BasicTextField(
                        value = price,
                        onValueChange = { onPriceChange(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        singleLine = true
                    )

                    if(price.isEmpty()) {
                        Text(
                            text = "Enter price",
                            fontFamily = Geist,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 14.4.sp,
                            color = Color(0xFFD9D9D9),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                HorizontalDivider()
            }

            Box(
                modifier = Modifier
                    .weight(1f)

            ) {
                Column() {
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .clickable{ dropdownExpanded = true },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = pricingUnit.title,
                            fontFamily = Geist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            lineHeight = 14.4.sp,
                            color = Color(0xFF212121),
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.weight(1f))

                        Image(
                            painter = painterResource(R.drawable.ic_dropdown),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                        )
                    }

                    HorizontalDivider()
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                ) {
                    PricingUnit.entries.forEach { entry ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = entry.title,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    lineHeight = 14.4.sp,
                                    color = Color(0xFF212121),
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = {
                                onPricingUnitChange(entry)
                                dropdownExpanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageBox(
    imageFile: File?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable{ onClick() },
        contentAlignment = Alignment.Center
    ) {
        if(imageFile != null) {
            AsyncImage(
                model = imageFile,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = "Click to add image",
                fontFamily = Geist,
                fontWeight = FontWeight.Light,
                fontSize = 10.sp,
                color = Color.Black.copy(alpha = 0.21f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AddItemButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontFamily = Geist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF749CF8).copy(alpha = if(enabled) 1f else 0.4f))
            .clickable{ onClick() }
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    )
}

private fun priceFieldText(price: Double): String {
    return if(price % 1.0 == 0.0) price.toInt().toString() else price.toString()
}

private fun createImageFile(context: Context): File {
    val imagesDir = File(context.filesDir, "product_images")
    if (!imagesDir.exists()) imagesDir.mkdirs()
    val file = File(imagesDir, "${UUID.randomUUID()}.jpg")
    file.createNewFile()
    return file
}

private fun cameraOutputUri(context: Context, file: File): Uri {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    // hand the camera app write access to this exact file
    val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
    val activities = context.packageManager.queryIntentActivities(
        captureIntent,
        PackageManager.MATCH_DEFAULT_ONLY
    )
    for(info in activities) {
        context.grantUriPermission(
            info.activityInfo.packageName,
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
    return uri
}

@Preview(showBackground = true)
@Composable
fun PreviewAddItemToInventoryView() {
    val isAddItemToInventoryViewVisible = remember { mutableStateOf(false) }
    AddItemToInventoryView(
        isAddItemToInventoryViewVisible,
        StoreItemType.Vegetables,
        {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditItemInInventoryView() {
    EditItemInInventoryView(
        item = sampleItemsList[0],
        onSave = {},
        onDismiss = {}
    )
}
