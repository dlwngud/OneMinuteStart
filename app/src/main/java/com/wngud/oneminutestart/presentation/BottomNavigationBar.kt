package com.wngud.oneminutestart.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    val bottomScreens = listOf(
        Screen.TimerScreen,
        Screen.StatisticsScreen,
        Screen.SettingScreen
    )

    AnimatedVisibility(
        visible = bottomScreens.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier,
        ) {
            bottomScreens.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    alwaysShowLabel = true,
                    label = {
                        Text(
                            text = item.name,
                            style = TextStyle(
                                fontSize = 12.sp
                            )
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.name
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