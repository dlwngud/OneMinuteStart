package com.wngud.oneminutestart

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wngud.oneminutestart.presentation.setting.SettingScreen
import com.wngud.oneminutestart.presentation.statistics.StatisticsScreen
import com.wngud.oneminutestart.presentation.timer.OneMinuteScreen
import com.wngud.oneminutestart.presentation.timer.TimerScreen
import kotlin.system.exitProcess

@Composable
fun Navigation(
    navController: NavHostController
) {
    val shouldExitApp = remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = Screen.TimerScreen.route) {
        composable(Screen.TimerScreen.route) {
            TimerScreen(
                navController = navController,
                onBackPressed = {
                    shouldExitApp.value = true
                }
            )
        }
        composable(Screen.StatisticsScreen.route) {
            StatisticsScreen(
                navController = navController,
                onBackPressed = {
                    navController.popBackStack(Screen.StatisticsScreen.route, false)
                    shouldExitApp.value = true
                }
            )
        }
        composable(Screen.SettingScreen.route) {
            SettingScreen(
                navController = navController,
                onBackPressed = {
                    navController.popBackStack(Screen.SettingScreen.route, false)
                    shouldExitApp.value = true
                }
            )
        }
        composable(
            Screen.OneMinuteScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                    nullable = false
                }
            )) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1L
            OneMinuteScreen(
                navController = navController,
                id = id
            )
        }
    }

    if (shouldExitApp.value) {
        ExitDialog { shouldExitApp.value = false }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ExitDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 광고 넣을 부분(이미지, 동영상)
                Image(
                    painter = painterResource(R.drawable.app_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                )
                Text(
                    text = "종료하시겠습니까?"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text("취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismissRequest()
                            exitProcess(0)
                        }
                    ) {
                        Text("종료")
                    }
                }
            }
        }
    }
}