package com.wngud.oneminutestart.presentation.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wngud.oneminutestart.utils.formatTime
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.StrokeCap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wngud.oneminutestart.MainViewModel
import com.wngud.oneminutestart.domain.TimerService
import com.wngud.oneminutestart.presentation.timer.TimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CircularStopWatch(
    title: String,
    context: Context,
    timerViewModel: TimerViewModel,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isDarkMode by mainViewModel.isDarkMode.collectAsState()
    var elapsedTime by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }

    val timerState by timerViewModel.timerState.collectAsStateWithLifecycle()

    LaunchedEffect(isRunning) {
        if (isRunning) {
            launch {
                while (isRunning) {
                    delay(1000)
                    elapsedTime++
                }
            }

            launch {
                while (isRunning) {
                    progress.animateTo(
                        targetValue = elapsedTime / 3599f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.size(350.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = if (isDarkMode) Color.White else Color.LightGray,
            progress = 1f,
            strokeWidth = 10.dp
        )

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = progress.value,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round,
            color = if (isDarkMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${formatTime(isLeadingZeroNeeded = true, value = elapsedTime / 3600)}:" +
                        "${
                            formatTime(isLeadingZeroNeeded = true, value = (elapsedTime / 60) % 60)
                        }:" +
                        formatTime(isLeadingZeroNeeded = true, value = elapsedTime % 60),
                fontSize = 48.sp,
                color = if (isDarkMode) Color.White else Color.Unspecified
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    modifier = Modifier.padding(4.dp),
                    contentDescription = null,
                    tint = if (isDarkMode) Color.White else Color.Unspecified
                )
                Text(
                    text = title,
                    color = if (isDarkMode) Color.White else Color.Unspecified
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isRunning = !isRunning
                    if(isRunning) {
                        val serviceIntent = Intent(context, TimerService::class.java).apply {
                            action = "START_TIMER"
                            putExtra("time",1*60*1000L)
                        }
                        timerViewModel.startTimer(1*60*1000L)
                        context.startForegroundService(serviceIntent)
                    } else {
                        val serviceIntent = Intent(context, TimerService::class.java).apply {
                            action = "PAUSE_TIMER"
                        }
                        context.startForegroundService(serviceIntent)
                    }
                },
                colors = ButtonColors(
                    MaterialTheme.colorScheme.primary,
                    contentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified,
                )
            ) {
                Text(text = if (isRunning) "멈춤" else "시작")
            }
        }
    }
}