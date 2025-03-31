package com.wngud.oneminutestart.presentation.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.StrokeCap
import androidx.hilt.navigation.compose.hiltViewModel
import com.wngud.oneminutestart.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun CircularCountDownTimer(
    title: String,
    oneMinuteEnd: () -> Unit
) {
    val viewModel: MainViewModel = hiltViewModel()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    var leftTime by remember { mutableStateOf(60) }
    val progress =
        remember { Animatable(leftTime / 60.0f) }
    val progressTarget = 0f
    var isZero by remember { mutableStateOf(false) }

    LaunchedEffect(isZero) {
        if (leftTime > 0) {
            while (leftTime > 0) {
                delay(1000)
                leftTime--
                if (leftTime == 0) {
                    isZero = true
                }
            }
        } else {
            oneMinuteEnd()
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = progressTarget,
            animationSpec = tween(
                durationMillis = 60 * 1000,
                easing = LinearEasing
            )
        )
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
                text = "${formatTime(isLeadingZeroNeeded = true, value = leftTime / 3600)}:" +
                        "${
                            formatTime(isLeadingZeroNeeded = true, value = (leftTime / 60) % 60)
                        }:" +
                        formatTime(isLeadingZeroNeeded = true, value = leftTime % 60),
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
        }
    }
}