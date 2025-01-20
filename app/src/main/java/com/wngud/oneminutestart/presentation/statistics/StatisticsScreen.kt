package com.wngud.oneminutestart.presentation.statistics

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.tooling.preview.Preview
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
import java.time.LocalDate
import java.time.YearMonth

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedChoiceIndex.value == 0) {
                        WeeklyGraph(isDarkMode)
                    } else {
                        // 월간 그래프
                        MonthGraph()
                    }
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

@Preview(showBackground = true)
@SuppressLint("NewApi")
@Composable
fun MonthGraph(
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now(),
    completedDays: List<Pair<Int, Int>> = listOf(
        1 to 4,
        2 to 0,
        3 to 9,
        4 to 2,
        5 to 10,
        6 to 1,
        7 to 7,
        8 to 5,
        9 to 3,
        10 to 0,
        11 to 8,
        12 to 6,
        13 to 2,
        14 to 10,
        15 to 4,
        16 to 7,
        17 to 1,
        18 to 9
    )
) {
    val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7 // 0 (Sunday) ~ 6 (Saturday)
    val today = currentDate.dayOfMonth

    fun getBackgroundColor(tasksCompleted: Int): Color {
        return when {
            tasksCompleted == 0 -> Color(0xFFE0E0E0) // Light gray
            tasksCompleted in 1..3 -> Color(0xFFFFF9C4) // Light yellow
            tasksCompleted in 4..6 -> Color(0xFFFFF59D) // Medium yellow
            tasksCompleted in 7..9 -> Color(0xFFFFEB3B) // Deep yellow
            tasksCompleted >= 10 -> Color(0xFFFBC02D) // Dark yellow
            else -> Color(0xFFE0E0E0) // Default
        }
    }

    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Less", color = Color.LightGray, fontSize = 12.sp)
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE0E0E0))
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFF9C4))
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFF59D))
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFEB3B))
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFBC02D))
            )
            Text(text = "More", color = Color.LightGray, fontSize = 12.sp)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.wrapContentWidth(),
        ) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            for (day in daysOfWeek) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Transparent)
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days Grid
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0 until rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val day = cellIndex - firstDayOfWeek + 1

                        if (cellIndex < firstDayOfWeek || day > daysInMonth) {
                            Spacer(modifier = Modifier.size(36.dp))
                        } else {
                            val tasksCompleted = completedDays.find { it.first == day }?.second ?: 0
                            val isToday = day == today

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isToday -> Color(0xFFBBDEFB) // Highlight today's date
                                            else -> getBackgroundColor(tasksCompleted)
                                        }
                                    )
                            ) {
                                Text(
                                    text = day.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isToday) Color.Black else Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}