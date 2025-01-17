package com.wngud.oneminutestart.presentation.statistics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wngud.oneminutestart.R
import com.wngud.oneminutestart.presentation.components.AppBar
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.StrokeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit,
) {
    val isDarkMode = isSystemInDarkTheme()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                ContinuedAchieveCard(maxDay = 15, current = 15)

                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row {
                    StatisticsItem(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        title = "완료한 작업",
                        content = "3024개",
                        variation = "+12% ↑"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatisticsItem(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        title = "총 집중 시간",
                        content = "1655시간",
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

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                WeeklyGraph(isDarkMode)
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
                fontSize = 18.sp,
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

@Composable
fun WeeklyGraph(
    isDarkMode: Boolean
) {
    val labelProperties = LabelProperties(
        enabled = true,
        textStyle = TextStyle(
            color = if (isDarkMode) Color.White else Color.Black
        ),
        labels = listOf("월", "화", "수", "목", "금", "토", "일")
    )

    val gridProperties = GridProperties(
        enabled = true,
        xAxisProperties = GridProperties.AxisProperties(
            enabled = true,
            color = Brush.linearGradient(colors = listOf(Color.LightGray, Color.LightGray)),
            style = StrokeStyle.Dashed(intervals = floatArrayOf(10f, 10f))
        ),
        yAxisProperties = GridProperties.AxisProperties(
            enabled = true,
            color = Brush.linearGradient(colors = listOf(Color.LightGray, Color.LightGray)),
            style = StrokeStyle.Dashed(intervals = floatArrayOf(10f, 10f)),
            lineCount = 7
        )
    )
    val indicatorProperties = HorizontalIndicatorProperties(
        enabled = true,
        textStyle = TextStyle(
            color = if (isDarkMode) Color.White else Color.Black
        )
    )
    val labelHelperProperties = LabelHelperProperties(
        enabled = true,
        textStyle = TextStyle(
            color = if (isDarkMode) Color.White else Color.Black
        )
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        data = remember {
            listOf(
                Line(
                    label = "운동하기",
                    values = listOf(28.0, 41.0, 5.0, 10.0, 35.0, 20.0, 25.0),
                    color = SolidColor(Color(0xFF23af92)),
                    firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.White),
                        strokeWidth = 2.dp,
                        radius = 5.dp,
                        strokeColor = SolidColor(Color(0xFF23af92)),
                    )
                ),
                Line(
                    label = "공부하기",
                    values = listOf(0.0, 15.0, 25.0, 8.0, 20.0, 0.0, 50.0),
                    color = SolidColor(Color(0xFF809D3C)),
                    firstGradientFillColor = Color(0xFFA9C46C).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.White),
                        strokeWidth = 2.dp,
                        radius = 5.dp,
                        strokeColor = SolidColor(Color(0xFF809D3C)),
                    )
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        labelProperties = labelProperties,
        gridProperties = gridProperties,
        indicatorProperties = indicatorProperties,
        labelHelperProperties = labelHelperProperties
    )
}

@Composable
fun ContinuedAchieveCard(
    maxDay: Int,
    current: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.achieve_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = buildAnnotatedString {
                        append("최대 연속\n")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${maxDay}일 ")
                        }
                        append("달성")
                    },
                    fontSize = 24.sp,
                    lineHeight = 40.sp
                )
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .size(100.dp),
                    alignment = Alignment.Center
                )
            }

            LinearProgressIndicator(
                progress = 0.2f,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth(),
                backgroundColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(8.dp))
            // 응원멘트
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (maxDay == current) {
                    Text(
                        text = "계속해서 만들어가자! \uD83D\uDCAA",
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "작은 발걸음이 큰 변화를 만들어! \uD83D\uDCAA",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}