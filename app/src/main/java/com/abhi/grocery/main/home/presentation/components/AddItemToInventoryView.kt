package com.abhi.grocery.main.home.presentation.components

import android.content.Context
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
import java.io.File
import java.util.UUID

@Composable
fun AddItemToInventoryView(
    isVisible: MutableState<Boolean>,
    onAdd: (ProductItem) -> Unit // call this when submitting
) {
    val context = LocalContext.current

    var type by remember { mutableStateOf<StoreItemType>(StoreItemType.Vegetables) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var pricingUnit by remember { mutableStateOf<PricingUnit>(PricingUnit.PER_KG) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }

    // registering the camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // imageUri now contains the captured image
            imageUri = pendingImageUri
        }
        pendingImageUri = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .clickable { isVisible.value = false }
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
                onTypeChange = { type = it }
            )

            Spacer(Modifier.height(30.dp))

            ImageBox(
                imageUri = imageUri
            ) {
                val uri = createImageUri(context)
                pendingImageUri = uri
                cameraLauncher.launch(uri)
            }

            Spacer(Modifier.height(8.dp))

            TextFields(
                type = type,
                name = name,
                onNameChange = { name = it },
                price = price,
                onPriceChange = { price = it },
                onPricingUnitChange = { pricingUnit = it }
            )

            Spacer(Modifier.height(30.dp))

            AddItemButton {
                onAdd(
                    ProductItem(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        hindiName = name,
                        imageName = "",
                        price = price.toDoubleOrNull() ?: 0.0,
                        pricingUnit = pricingUnit,
                        productType = type
                    )
                )
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
    onPricingUnitChange: (PricingUnit) -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var pricingIndex by remember { mutableStateOf<Int>(0) }
    
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
                            text = PricingUnit.entries[pricingIndex].title,
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
                    PricingUnit.entries.forEachIndexed { index, entry ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = entry.unit,
                                    fontFamily = Geist,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    lineHeight = 14.4.sp,
                                    color = Color(0xFFD9D9D9),
                                    textAlign = TextAlign.Center,
                                )
                            },
                            onClick = {
                                pricingIndex = index
                                onPricingUnitChange(PricingUnit.entries[index])
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
    imageUri: Uri?,
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
        if(imageUri != null) {
            AsyncImage(
                model = imageUri,
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
    onClick: () -> Unit
) {
    Text(
        text = "Add Item",
        fontFamily = Geist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF749CF8))
            .clickable{ onClick() }
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    )
}

private fun createImageUri(context: Context): Uri {
    val file = File(
        context.cacheDir,
        "captured_${System.currentTimeMillis()}.jpg"
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddItemToInventoryView() {
    val isAddItemToInventoryViewVisible = remember { mutableStateOf<Boolean>(false) }
    AddItemToInventoryView(
        isAddItemToInventoryViewVisible,
        {}
    )
}