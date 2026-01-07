package `in`.co.abdev.greenguardian.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class ThemeState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

class ThemeViewModel : ViewModel() {
    
    private val _themeState = MutableStateFlow(ThemeState())
    val themeState: StateFlow<ThemeState> = _themeState.asStateFlow()
    
    fun setThemeMode(mode: ThemeMode) {
        _themeState.value = _themeState.value.copy(themeMode = mode)
    }
}
