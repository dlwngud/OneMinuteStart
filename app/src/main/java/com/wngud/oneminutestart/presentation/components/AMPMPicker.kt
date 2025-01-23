package com.wngud.oneminutestart.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AMPMPicker(numbers: List<String>, selectedItem: (String) -> Unit) {
    val listState = rememberLazyListState(0, 0)
    val coroutineScope = rememberCoroutineScope()

    val isScrollInProgress = remember { derivedStateOf { listState.isScrollInProgress } }
    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }

    val textSizeState = remember { mutableStateOf(22.sp) }
    val textColorState = remember { mutableStateOf(Color.Black) }

    Box(
        modifier = Modifier
            .width(40.dp)
            .height(120.dp)
    ) {
        LazyColumn(
            state = listState
        ) {
            itemsIndexed(items = numbers) { index, item ->
                if (firstVisibleItemIndex.value == index - 1) {
                    textColorState.value = Color.Black
                    selectedItem(item)
                } else {
                    textColorState.value = Color.Gray
                }
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        fontSize = textSizeState.value,
                        textAlign = TextAlign.Center,
                        color = textColorState.value
                    )
                }
            }
        }

        val density = LocalDensity.current
        if (!isScrollInProgress.value) {
            coroutineScope.launch {
                val itemHeight = with(density) { 40.dp.toPx() }
                val offset = firstVisibleItemScrollOffset.value + (itemHeight / 2)
                val targetIndex = if (offset < itemHeight) {
                    firstVisibleItemIndex.value
                } else {
                    firstVisibleItemIndex.value + 1
                }

                coroutineScope.launch {
                    listState.animateScrollToItem(targetIndex.coerceIn(0, numbers.size - 1))
                }
            }
        }
    }
}