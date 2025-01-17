package com.wngud.oneminutestart.presentation.statistics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.presentation.components.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit,
) {
    val choices = remember { mutableStateListOf("주간", "월간") }
    var selectedChoiceIndex = remember { mutableIntStateOf(0) }

    BackHandler {
        onBackPressed()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "통계",
                hasBackButton = false,
                action = {
                    SingleChoiceSegmentedButtonRow {
                        choices.forEachIndexed { index, choice ->
                            SegmentedButton(
                                selected = selectedChoiceIndex.intValue == index,
                                onClick = { selectedChoiceIndex.intValue = index },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = choices.size
                                ),
                                icon = {},
                                border = BorderStroke(0.dp, Color.Transparent)
                            ) {
                                Text(choice)
                            }
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    StatisticsItem(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        title = "완료한 작업",
                        content = "32개",
                        variation = "+12% ↑"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatisticsItem(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        title = "총 집중 시간",
                        content = "16시간",
                        variation = "+8% ↑"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatisticsItem(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        title = "목표 달성률",
                        content = "85%",
                        variation = "-2.1% ↓"
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsItem(
    modifier: Modifier,
    title: String,
    content: String,
    variation: String
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title
            )
            Text(
                text = content,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 39.sp
            )
            Text(
                text = variation,
                color = if (variation[0] == '+') Color(0xFF4CAF50) else Color(0xFFDB4455),
                fontWeight = FontWeight.Bold
            )
        }
    }
}