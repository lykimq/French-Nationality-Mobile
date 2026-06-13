package com.lykimq_uyen.french_nationality.core.settings

interface AppPreferencesRepository {
    fun getThemeMode(): ThemeMode

    fun saveThemeMode(themeMode: ThemeMode)
}
