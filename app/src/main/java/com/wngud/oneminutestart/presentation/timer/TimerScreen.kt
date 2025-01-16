package com.wngud.oneminutestart.presentation.timer

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.Screen
import com.wngud.oneminutestart.domain.Task
import com.wngud.oneminutestart.presentation.components.AMPMPicker
import com.wngud.oneminutestart.presentation.components.AppBar
import com.wngud.oneminutestart.presentation.components.NumberPicker
import kotlinx.coroutines.launch

@Composable
fun TimerScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("1분만 해보자!", "더 해볼래?")
    val pagerState = rememberPagerState(pageCount = { 2 })
    var showTaskDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val dummyTasks = listOf(
        Task(
            id = 1,
            title = "운동하기",
            reminderTime = "07:00",
            isCompletedOneMinute = false
        ),
        Task(
            id = 2,
            title = "공부하기",
            reminderTime = null,
            isCompletedOneMinute = false
        ),
        Task(
            id = 3,
            title = "밥먹기",
            reminderTime = "12:00",
            isCompletedOneMinute = false
        ),
        Task(
            id = 4,
            title = "명상하기",
            reminderTime = "22:00",
            isCompletedOneMinute = true
        )
    )
    val dummyEmptyList = listOf<Task>()

    if (showTaskDialog) {
        TaskDialog(
            task = null,
            title = "수정",
            onDismissDialog = {
                showTaskDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DeleteDialog(
            task = dummyTasks[0],
            onDismissDialog = {
                showDeleteDialog = false
            }
        )
    }

    BackHandler {
        onBackPressed()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "안녕하세요, 쭉가님\n바로 시작해 볼까요?", hasBackButton = false
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        },
                        unselectedContentColor = Color.Gray
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> TaskListTab(
                        tasks = dummyTasks.filter { !it.isCompletedOneMinute },
                        emptyMessage = "미루지 말고, 지금 시작할 작업을 추가해보세요\n" +
                                "작은 시작이 큰 변화를 만들어낼 수 있어요",
                        buttonText = "일단 시작!",
                        onButtonClick = { task ->
                            navController.navigate(Screen.OneMinuteScreen.createRoute(task.id))
                        },
                        page = page,
                        onDialogRequested = { showTaskDialog = true },
                        onDismissDialog = { showTaskDialog = false },
                        onDeleteDialogRequested = { showDeleteDialog = true },
                        onDismissDeleteDialog = { showDeleteDialog = false }
                    )

                    1 -> TaskListTab(
                        tasks = dummyTasks.filter { it.isCompletedOneMinute },
                        emptyMessage = "1분만이라도 해보세요\n" +
                                "짧은 시간 안에 성취감을 느낄 수 있을 거예요\n" +
                                "그 작은 시작이 여러분을 더 많은 작업으로 이끌어줄 거예요",
                        buttonText = "더 해볼래!",
                        onButtonClick = { task ->
                            navController.navigate(Screen.MoreScreen.createRoute(task.id))
                        },
                        page = page,
                        onDialogRequested = { showTaskDialog = true },
                        onDismissDialog = { showTaskDialog = false },
                        onDeleteDialogRequested = { showDeleteDialog = true },
                        onDismissDeleteDialog = { showDeleteDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskListTab(
    tasks: List<Task>,
    emptyMessage: String,
    buttonText: String,
    page: Int,
    onButtonClick: (Task) -> Unit,
    onDialogRequested: () -> Unit,
    onDismissDialog: () -> Unit,
    onDeleteDialogRequested: () -> Unit,
    onDismissDeleteDialog: () -> Unit
) {
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    var selectedTaskId by remember { mutableStateOf<Long?>(null) }

    if (tasks.isEmpty()) {
        if (page == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDialogRequested()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "추가"
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(tasks) { task ->
                SwipeTask(
                    task = task,
                    buttonText = buttonText,
                    onClick = { onButtonClick(task) },
                    height = itemHeight,
                    onItemHeightChange = { size ->
                        itemHeight = with(density) { size.height.toDp() }
                    },
                    isSelected = selectedTaskId == task.id,
                    onSelect = { isSelected ->
                        selectedTaskId = if (isSelected) task.id else null
                    },
                    onDialogRequested = onDialogRequested,
                    onDismissDialog = onDismissDialog,
                    onDeleteDialogRequested = onDeleteDialogRequested,
                    onDismissDeleteDialog = onDismissDeleteDialog
                )
            }
            if (page == 0) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(itemHeight),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDialogRequested()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("추가")
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeTask(
    task: Task,
    buttonText: String,
    onClick: () -> Unit,
    onItemHeightChange: (IntSize) -> Unit,
    height: Dp,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    onDialogRequested: () -> Unit,
    onDismissDialog: () -> Unit,
    onDeleteDialogRequested: () -> Unit,
    onDismissDeleteDialog: () -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val coroutineScope = rememberCoroutineScope()
    val squareSize = 80.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(
        -sizePx to -1, // 왼쪽으로 스와이프 (수정 버튼)
        0f to 0,       // 원래 위치
        sizePx to 1    // 오른쪽으로 스와이프 (삭제 버튼)
    )

    LaunchedEffect(isSelected) {
        if (!isSelected && swipeableState.currentValue != 0) {
            swipeableState.animateTo(0, tween(600, 0))
        }
    }

    LaunchedEffect(swipeableState.currentValue) {
        if (swipeableState.currentValue != 0) {
            onSelect(true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                orientation = Orientation.Horizontal,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                velocityThreshold = 1000.dp
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart) // 왼쪽에 배치
        ) {
            TextButton(
                modifier = Modifier
                    .width(100.dp)
                    .height(height)
                    .padding(horizontal = 16.dp),
                onClick = {
                    onDialogRequested()
                    coroutineScope.launch {
                        swipeableState.animateTo(0, tween(600, 0))
                    }
                },
                colors = ButtonColors(
                    Color(0xFF4CAF50),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            TextButton(
                modifier = Modifier
                    .width(100.dp)
                    .height(height)
                    .padding(horizontal = 16.dp),
                onClick = {
                    onDeleteDialogRequested()
                    coroutineScope.launch {
                        swipeableState.animateTo(0, tween(600, 0))
                    }
                },
                colors = ButtonColors(
                    Color(0xFFDB4455),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }

        Box(modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        onItemHeightChange(coordinates.size)
                    },
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        if (!task.reminderTime.isNullOrEmpty()) {
                            Text(
                                text = "리마인드 알림: ${task.reminderTime}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(
                                text = "리마인드 알림없음",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = buttonText)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    task: Task?,
    title: String,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissDialog() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "작업 이름",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 16.dp)
                )
                OutlinedTextField(
                    value = "dd",
                    onValueChange = { },
                    label = {
                        Text(
                            "작업 이름",
                            fontWeight = FontWeight.Thin
                        )
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "리마인드 알람",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = true,
                        onCheckedChange = {}
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AMPMPicker(AMPM_LIST) { }
                    Row {
                        NumberPicker(numbers = HOUR_LIST) {

                        }
                        Text(text = "시", modifier = Modifier.align(Alignment.CenterVertically))
                    }

                    Row {
                        NumberPicker(numbers = MINUTE_LIST) {

                        }
                        Text(text = "분", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismissDialog()
                        }
                    ) {
                        Text("취소")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismissDialog()
                        }
                    ) {
                        Text("추가")
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteDialog(
    task: Task,
    onDismissDialog: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()

    Dialog(onDismissRequest = { onDismissDialog() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "작업을 삭제할까요?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFFDB4455),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "삭제한 작업은 복구할 수 없어요.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            onDismissDialog()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = "취소",
                            color = if (isDarkMode) Color.White else Color.Unspecified
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDB4455)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onDismissDialog()
                        }
                    ) {
                        Text(
                            text = "삭제하기",
                            color = if (isDarkMode) Color.White else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}

val HOUR_LIST = (-1..13).toList()
val MINUTE_LIST = (-1..60).toList()
val AMPM_LIST = listOf("", "오전", "오후", "")