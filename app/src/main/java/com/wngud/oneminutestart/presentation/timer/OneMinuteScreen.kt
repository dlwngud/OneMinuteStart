package com.wngud.oneminutestart.presentation.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.presentation.components.AppBar
import com.wngud.oneminutestart.presentation.components.CircularCountDownTimer

@Composable
fun OneMinuteScreen(
    navController: NavHostController,
    id: Long
) {
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
            CircularCountDownTimer("운동하기")
        }
    }
}