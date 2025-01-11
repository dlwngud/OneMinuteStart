package com.wngud.oneminutestart.presentation.timer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.presentation.AppBar

@Composable
fun TimerScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    BackHandler {
        onBackPressed()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "타이머", hasBackButton = false
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.padding(it)) {

        }
    }
}