package com.wngud.oneminutestart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.oneminutestart.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
): ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        viewModelScope.launch {
            preferenceManager.isDarkMode.collect { savedMode ->
                _isDarkMode.value = savedMode
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            _isDarkMode.value = isDark
            preferenceManager.setDarkMode(isDark)
        }
    }
}