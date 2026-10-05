package com.abhi.grocery.main.home.presentation.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.abhi.grocery.R
import com.abhi.grocery.common.utils.rememberScreenSize
import com.abhi.grocery.common.utils.sampleItemsList
import com.abhi.grocery.main.home.domain.ProductItem
import com.abhi.grocery.ui.theme.Geist
import java.io.File

@Composable
fun ProductItemVendorView(
    productItem: ProductItem,
    onClick: (ProductItem) -> Unit,
    context: Context,
    borderColor: Color = Color(0xffd9d9d9),
) {
    val screenSize = rememberScreenSize()
    val cardWidth: Dp = (screenSize.width - (40.dp + 7.dp)) / 2

    val amountString = "₹" + productItem.price.toString()

    val imageFile = remember(productItem.imageName) { productImageFile(context, productItem.imageName) }

    Column(
        modifier = Modifier
            .width(cardWidth)
            .clip(shape = RoundedCornerShape(30.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable{
                onClick(productItem)
            }
            .padding(horizontal = 20.dp)
            .padding(vertical = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if(imageFile != null && imageFile.exists()) {
            AsyncImage(
                model = imageFile,
                contentDescription = null,
                modifier = Modifier
                    .size(cardWidth - 70.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_vendor_mode),
                contentDescription = null,
                modifier = Modifier
                    .size(cardWidth - 70.dp) // 35dp spacing each side horizontally
            )
        }

        Column(
            modifier = Modifier
                .width(cardWidth - 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = productItem.name,
                fontFamily = Geist,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amountString,
                    fontFamily = Geist,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                Text(
                    text = productItem.pricingUnit.title,
                    fontFamily = Geist,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 16.sp
                )
            }

        }


    }
}

fun productImageFile(context: Context, imageName: String): File? {
    if (imageName.isBlank()) return null
    return File(File(context.filesDir, "product_images"), imageName)
}

@Preview (showBackground = true)
@Composable
fun PreviewProductItemVendorView() {
    ProductItemVendorView(
        productItem = sampleItemsList[0],
        onClick = {},
        context = LocalContext.current
    )
}