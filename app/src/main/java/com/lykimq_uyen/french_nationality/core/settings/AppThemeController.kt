package com.lykimq_uyen.french_nationality.core.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppThemeController(
    private val appPreferencesRepository: AppPreferencesRepository,
) {
    private val _themeMode = MutableStateFlow(appPreferencesRepository.getThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(themeMode: ThemeMode) {
        appPreferencesRepository.saveThemeMode(themeMode)
        _themeMode.value = themeMode
    }
}
