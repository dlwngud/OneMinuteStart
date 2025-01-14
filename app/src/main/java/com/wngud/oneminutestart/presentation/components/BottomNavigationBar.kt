package com.wngud.oneminutestart.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wngud.oneminutestart.Screen

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isDarkTheme = isSystemInDarkTheme()

    val bottomScreens = listOf(
        Screen.TimerScreen,
        Screen.StatisticsScreen,
        Screen.SettingScreen
    )

    AnimatedVisibility(
        visible = bottomScreens.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar {
            bottomScreens.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    alwaysShowLabel = true,
                    label = {
                        Text(
                            text = item.name,
                            style = TextStyle(
                                fontSize = 12.sp
                            ),
                            color = if (currentRoute == item.route) {
                                if (isDarkTheme) Color.White
                                else Color.Black
                            } else {
                                Color.Gray
                            }
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.name,
                            tint = if (currentRoute == item.route) {
                                if (isDarkTheme) Color.White
                                else Color.Black
                            } else {
                                Color.Gray
                            }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                        }
                    }
                )
            }
        }
    }
}