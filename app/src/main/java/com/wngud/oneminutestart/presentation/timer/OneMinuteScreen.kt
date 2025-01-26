package com.wngud.oneminutestart.presentation.timer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.presentation.components.AppBar
import com.wngud.oneminutestart.presentation.components.CircularCountDownTimer

@Composable
fun OneMinuteScreen(
    navController: NavHostController,
    id: Long,
    timerViewModel: TimerViewModel
) {
    val taskState by timerViewModel.timerState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("fdasfas", taskState.tasks.toString())
        Log.d("fdasfas", taskState.detailTask.toString())
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "",
                hasBackButton = true,
                onBackNavClicked = {
                    navController.navigateUp()
                },
                action = {})
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CircularCountDownTimer(taskState.detailTask.title)
        }
    }
}