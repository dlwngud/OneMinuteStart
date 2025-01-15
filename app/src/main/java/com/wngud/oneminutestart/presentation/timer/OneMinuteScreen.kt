package com.wngud.oneminutestart.presentation.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.presentation.components.CircularCountDownTimer

@Composable
fun OneMinuteScreen(
    navController: NavHostController,
    id: Long
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        CircularCountDownTimer("운동하기")
    }
}