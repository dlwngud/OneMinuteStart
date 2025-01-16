package com.wngud.oneminutestart.presentation.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import com.wngud.oneminutestart.R
import com.wngud.oneminutestart.presentation.components.AppBar
import com.wngud.oneminutestart.presentation.components.CircularStopWatch

@Composable
fun MoreScreen(
    navController: NavHostController,
    id: Long
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            AppBar(
                title = "",
                hasBackButton = true,
                onBackNavClicked = {
                    navController.navigateUp()
                })
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            CircularStopWatch("운동하기")

            Spacer(modifier = Modifier.height(32.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((1..15).toList()) { whiteNoise ->
                    WhiteNoiseItem(whiteNoise)
                }
            }
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
                    .align(Alignment.CenterHorizontally)
                    ,
                painter = painterResource(id = R.drawable.ic_water),
                contentDescription = null,
                tint = if(isEnable) Color.Unspecified else Color.LightGray
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