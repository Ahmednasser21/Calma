package com.metafortech.calma.presentation.home

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.AppRoute
import com.metafortech.calma.presentation.AppRoute.ChattingScreen
import com.metafortech.calma.presentation.AppRoute.ReelsScreen
import com.metafortech.calma.presentation.AppRoute.HomeScreen
import com.metafortech.calma.presentation.AppRoute.SportsFacilitiesScreen
import com.metafortech.calma.presentation.AppRoute.StoreScreen

sealed class BottomNavItem(
    val route: AppRoute,
    val title: Int,
    val selectedIcon:Int,
    val unselectedIcon: Int
) {
    object Facilities : BottomNavItem(
        route = SportsFacilitiesScreen,
        title = R.string.facilities,
        selectedIcon = R.drawable.playground_selected ,
        unselectedIcon = R.drawable.playground_not_selected
    )

    object Reels : BottomNavItem(
        route = ReelsScreen,
        title = R.string.reels,
        selectedIcon = R.drawable.reels_selected,
        unselectedIcon = R.drawable.reels_not_selected
    )

    object Home : BottomNavItem(
        route = HomeScreen,
        title = R.string.home,
        selectedIcon = R.drawable.home_selected,
        unselectedIcon = R.drawable.home_not_selected
    )

    object Store : BottomNavItem(
        route = StoreScreen,
        title = R.string.store,
        selectedIcon = R.drawable.cart_selected,
        unselectedIcon = R.drawable.cart_not_selected
    )

    object Chat : BottomNavItem(
        route =ChattingScreen,
        title = R.string.chat,
        selectedIcon = R.drawable.chat_selected,
        unselectedIcon = R.drawable.chat_not_selected
    )
}

@Composable
fun BottomNavigationBar(
    selectedRoute: String?,
    onItemSelected: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Facilities,
        BottomNavItem.Reels,
        BottomNavItem.Home,
        BottomNavItem.Store,
        BottomNavItem.Chat
    )

    NavigationBar(
        modifier = modifier.shadow(24.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 24.dp
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute?.contains(item.route::class.simpleName ?: "") == true

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (isSelected) {
                           painterResource(item.selectedIcon)
                        } else {
                            painterResource(item.unselectedIcon)
                        },
                        contentDescription = stringResource(item.title),
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.title),
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                },
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}