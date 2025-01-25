package com.wngud.oneminutestart.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.oneminutestart.domain.Task
import com.wngud.oneminutestart.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TimerEvent {
    object Loading : TimerEvent()
    data class LoadTasks(val tasks: List<Task>) : TimerEvent()
    data class AddTask(val task: Task) : TimerEvent()
    data class UpdateTask(val task: Task) : TimerEvent()
    data class DeleteTask(val taskId: Long) : TimerEvent()
    data class LoadDetailTask(val taskId: Long) : TimerEvent()
    object StartTimer : TimerEvent()
    object StopTimer : TimerEvent()
    object ResetTimer : TimerEvent()
    object StartStopwatch : TimerEvent()
    object StopStopwatch : TimerEvent()
    object ResetStopwatch : TimerEvent()
}

sealed class TimerSideEffect {
    data class ShowSnackBar(val message: String) : TimerSideEffect()
    data class NavigateToOne(val itemId: Long) : TimerSideEffect()
    data class NavigateToMore(val itemId: Long) : TimerSideEffect()
}

data class TimerState(
    val tasks: List<Task> = emptyList(),
    val detailTask: Task = Task(),
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val events = Channel<TimerEvent>()

    val timerState: StateFlow<TimerState> = events.receiveAsFlow()
        .runningFold(TimerState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerState())

    private val _sideEffects = Channel<TimerSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: TimerState, event: TimerEvent): TimerState {
        return when (event) {
            TimerEvent.Loading -> current.copy(loading = true)
            is TimerEvent.LoadTasks -> current.copy(loading = false, tasks = event.tasks)
            is TimerEvent.AddTask -> TODO()
            is TimerEvent.DeleteTask -> TODO()
            TimerEvent.ResetStopwatch -> TODO()
            TimerEvent.ResetTimer -> TODO()
            TimerEvent.StartStopwatch -> TODO()
            TimerEvent.StartTimer -> TODO()
            TimerEvent.StopStopwatch -> TODO()
            TimerEvent.StopTimer -> TODO()
            is TimerEvent.UpdateTask -> TODO()
            is TimerEvent.LoadDetailTask -> current.copy(
                loading = false,
                detailTask = current.tasks.find { it.id == event.taskId } ?: Task())
        }
    }

    fun postEffect(timerSideEffect: TimerSideEffect) {
        viewModelScope.launch {
            _sideEffects.send(timerSideEffect)
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            events.send(TimerEvent.Loading)
            taskRepository.getAllTasks().collectLatest { tasks ->
                events.send(TimerEvent.LoadTasks(tasks))
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            events.send(TimerEvent.Loading)
            taskRepository.saveTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            events.send(TimerEvent.Loading)
            taskRepository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            events.send(TimerEvent.Loading)
            taskRepository.updateTask(task)
        }
    }

    fun loadDetailTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId).collectLatest { task ->
                events.send(TimerEvent.LoadDetailTask(task.id))
            }
        }
    }

    fun startTimer() {
        // 타이머 시작 로직
    }

    fun stopTimer() {
        // 타이머 정지 로직
    }

    fun resetTimer() {
        // 타이머 초기화 로직
    }

    fun startStopwatch() {
        // 스톱워치 시작 로직
    }

    fun stopStopwatch() {
        // 스톱워치 정지 로직
    }

    fun resetStopwatch() {
        // 스톱워치 초기화 로직
    }
}