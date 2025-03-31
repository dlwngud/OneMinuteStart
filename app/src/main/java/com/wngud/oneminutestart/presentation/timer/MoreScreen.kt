package com.wngud.oneminutestart.presentation.timer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.R
import com.wngud.oneminutestart.domain.TimerService
import com.wngud.oneminutestart.presentation.components.AppBar
import com.wngud.oneminutestart.presentation.components.CircularStopWatch

@Composable
fun MoreScreen(
    navController: NavHostController,
    id: Long,
    timerViewModel: TimerViewModel
) {
    val taskState by timerViewModel.timerState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = getActivityFromCompose()
    var elapsedTime by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "TIMER_UPDATE" -> {
                    elapsedTime = intent.getLongExtra("elapsedTime", 0L)
                    isRunning = intent.getBooleanExtra("isRunning", false)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val intentFilter = IntentFilter().apply {
            addAction("TIMER_UPDATE")
        }

        ContextCompat.registerReceiver(
            context,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(receiver)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            AppBar(
                title = "",
                hasBackButton = true,
                onBackNavClicked = {
                    navController.navigateUp()
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                },
                action = {}
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                CircularStopWatch(
                    onStart = {
                        val serviceIntent = Intent(context, TimerService::class.java).apply {
                            action = "START_TIMER"
                        }
                        context.startForegroundService(serviceIntent)
                    },
                    onStop = {
                        val serviceIntent = Intent(context, TimerService::class.java).apply {
                            action = "PAUSE_TIMER"
                        }
                        context.startForegroundService(serviceIntent)
                    },
                    onReset = {
                        val serviceIntent = Intent(context, TimerService::class.java).apply {
                            action = "STOP_TIMER"
                        }
                        context.startForegroundService(serviceIntent)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Grid(columns = 3) {
                    WhiteNoiseItem(1)
                }
            }
        }
    }
}

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    columns: Int,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        for (i in 0 until (15 + columns - 1) / columns) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 0 until columns) {
                    val index = i * columns + j
                    if (index < 15) {
                        Box(modifier = Modifier.weight(1f)) {
                            content()
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WhiteNoiseItem(
    a: Int
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isEnable by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            MaterialTheme.colorScheme.primary,
            contentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified,
        )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    isEnable = !isEnable
                }
                .padding(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_water),
                contentDescription = null,
                tint = if (isEnable) Color.Unspecified else Color.LightGray
            )
            Slider(
                value = sliderPosition,
                onValueChange = { newVolume ->
                    sliderPosition = newVolume
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 14,
                valueRange = 0f..1f,
                enabled = isEnable
            )
        }
    }
}

@Composable
fun getActivityFromCompose(): Activity? {
    val context = LocalContext.current
    return context.findActivity()
}

// Activity를 찾아내는 확장 함수
fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}