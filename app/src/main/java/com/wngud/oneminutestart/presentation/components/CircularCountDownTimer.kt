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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wngud.oneminutestart.utils.formatTime
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.delay

@Composable
fun CircularCountDownTimer(
    title: String
) {
    var leftTime by remember { mutableStateOf(60) }
    val progress =
        remember { Animatable(leftTime / 60.0f) }
    val progressTarget = 0f

    LaunchedEffect(Unit) {
        while(leftTime > 0) {
            delay(1000)
            leftTime--
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
            color = Color.LightGray,
            progress = 1f,
            strokeWidth = 10.dp
        )

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = progress.value,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${formatTime(isLeadingZeroNeeded = true, value = leftTime / 3600)}:" +
                        "${
                            formatTime(isLeadingZeroNeeded = true, value = (leftTime / 60) % 60)
                        }:" +
                        formatTime(isLeadingZeroNeeded = true, value = leftTime % 60),
                fontSize = 48.sp

            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    modifier = Modifier.padding(4.dp),
                    contentDescription = null
                )
                Text(
                    text = title
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun A() {
    CircularCountDownTimer("운동하기")
}