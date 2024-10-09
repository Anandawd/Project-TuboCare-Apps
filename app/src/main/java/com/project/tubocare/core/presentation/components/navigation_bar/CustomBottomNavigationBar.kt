package com.project.tubocare.core.presentation.components.navigation_bar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.core.util.MainRouteScreen
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit
) {
    NavigationBar(
        modifier = modifier.height(60.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 10.dp
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        items.forEachIndexed { index, item ->

            val isSelected = item.route == currentRoute
            val iconId = if (isSelected) item.selectedIconId else item.unSelectedIconId
            val iconAlpha = if (isSelected) 1f else 0.5f

            if( index == 2){
                Spacer(modifier = Modifier.weight(1f))
            }
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item) },
                icon = {
                    BadgedBox(badge = {
                        if (item.badgeCount != null){
                            Badge{
                                Text(text = item.badgeCount.toString())
                            }
                        } else if (item.hasBadge){
                            Badge()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = item.title,
                            tint = Color.White.copy(alpha = iconAlpha)
                        )

                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.White.copy(alpha = iconAlpha),
                    selectedIndicatorColor = Color.Transparent,
                    selectedTextColor = Color.White.copy(alpha = iconAlpha),
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f),
                    disabledIconColor = Color.White.copy(alpha = 0.5f),
                    disabledTextColor = Color.White.copy(alpha = 0.5f)
                ),
                alwaysShowLabel = false
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}



@Preview
@Composable
private fun CustomBottomNavigationPreview() {
    TuboCareTheme {
        CustomBottomNavigation(
            items = list_menu,
            currentRoute = MainRouteScreen.Medication.route,
            onItemClick = {}
        )
    }
}