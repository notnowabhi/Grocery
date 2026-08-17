package com.abhi.grocery.main.uicomponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhi.grocery.main.Tab
import com.abhi.grocery.ui.theme.Geist

@Composable
fun CustomTabBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(50)) // capsule shape
            .background(color = Color(0xFF212121))
            .clickable{ }
            .padding(vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 13.dp)
        ) {
            Tab.entries.forEach{ tab ->
                TabItem(
                    isSelected = selectedTab == tab,
                    iconId = tab.iconId,
                    title = tab.title,
                    onClick = {onTabSelected(tab)}
                )
            }
        }
    }
}

@Composable
fun TabItem(
    isSelected: Boolean,
    iconId: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = if (isSelected) Color.White else Color(0xFFCCCCCC),
                shape = RoundedCornerShape(50)
            )
            .clickable{onClick()}
            .padding(vertical = 11.dp)
            .padding(start = if(isSelected) 14.dp else 11.dp)
            .padding(end = if(isSelected) 16.dp else 11.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "icon",
            modifier = modifier
                .size(16.dp)
        )

        if(isSelected) {
            Text(
                text = title,
                fontFamily = Geist,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                lineHeight = 12.sp,

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTabBarPreview() {
    var selectedTab by remember { mutableStateOf(Tab.Home) }

    CustomTabBar(
        selectedTab = selectedTab,   // use a valid enum value
        onTabSelected = {selectedTab = it},
        modifier = Modifier
    )
}