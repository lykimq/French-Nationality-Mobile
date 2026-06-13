package com.lykimq_uyen.french_nationality.core.progress

import android.content.Context

class StudyProgressRepositoryImpl(
    context: Context,
) : StudyProgressRepository {

    private val preferences = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getLastQuestionId(subCategoryId: String): String? {
        return preferences.getString(preferenceKey(subCategoryId), null)
    }

    override fun saveLastQuestionId(subCategoryId: String, questionId: String) {
        preferences.edit()
            .putString(preferenceKey(subCategoryId), questionId)
            .apply()
    }

    override fun clearLastQuestionId(subCategoryId: String) {
        preferences.edit()
            .remove(preferenceKey(subCategoryId))
            .apply()
    }

    override fun clearAllProgress() {
        preferences.edit()
            .clear()
            .apply()
    }

    private fun preferenceKey(subCategoryId: String): String {
        return "$KEY_PREFIX$subCategoryId"
    }

    companion object {
        private const val PREFS_NAME = "study_progress"
        private const val KEY_PREFIX = "last_question_"
    }
}
