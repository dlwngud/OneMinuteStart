package com.wngud.oneminutestart.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wngud.oneminutestart.MainViewModel
import com.wngud.oneminutestart.Screen

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val viewModel: MainViewModel = hiltViewModel()
    val isDarkTheme by viewModel.isDarkMode.collectAsState()

    val bottomScreens = listOf(
        Screen.TimerScreen,
        Screen.StatisticsScreen,
        Screen.SettingScreen
    )

    AnimatedVisibility(
        visible = bottomScreens.map { it.route }.contains(currentRoute)
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            bottomScreens.forEach { item ->
                BottomNavigationItem(
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