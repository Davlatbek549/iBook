package com.example.dz.navigation


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomBottomBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    selectedContentColor: Color? = null,
    unselectedContentColor: Color? = null
) {
    val resolvedContainerColor = containerColor ?: MaterialTheme.colorScheme.primary
    val resolvedSelectedContentColor = selectedContentColor ?: MaterialTheme.colorScheme.onPrimary
    val resolvedUnselectedContentColor =
        unselectedContentColor ?: MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.74f)

    Box(
        modifier = modifier
            .padding(horizontal = 22.dp, vertical = 10.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color.Black.copy(alpha = 0.18f),
                spotColor = Color.Black.copy(alpha = 0.28f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(resolvedContainerColor)
            .fillMaxWidth()
            .height(58.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->

                val isSelected = currentRoute == item.route

                val tint by animateColorAsState(
                    targetValue = if (isSelected)
                        resolvedSelectedContentColor
                    else
                        resolvedUnselectedContentColor,
                    label = "iconColor"
                )

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.12f else 1f,
                    label = "iconScale"
                )

                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.route,
                    tint = tint,
                    modifier = Modifier
                        .size(22.dp)
                        .scale(scale)
                        .clickable {
                            onItemClick(item.route)
                        }
                )
            }
        }
    }
}
