package com.lykimq_uyen.french_nationality.core.di

import android.content.Context
import com.lykimq_uyen.french_nationality.core.progress.StudyProgressRepository
import com.lykimq_uyen.french_nationality.core.progress.StudyProgressRepositoryImpl
import com.lykimq_uyen.french_nationality.core.settings.AppPreferencesRepository
import com.lykimq_uyen.french_nationality.core.settings.AppPreferencesRepositoryImpl
import com.lykimq_uyen.french_nationality.core.settings.AppThemeController
import com.lykimq_uyen.french_nationality.core.speech.FrenchSpeechController
import com.lykimq_uyen.french_nationality.core.speech.SpeechPreferencesRepository
import com.lykimq_uyen.french_nationality.core.speech.SpeechPreferencesRepositoryImpl
import com.lykimq_uyen.french_nationality.data.db.AppDatabase
import com.lykimq_uyen.french_nationality.feature.home.data.CategoryRepositoryImpl
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository
import com.lykimq_uyen.french_nationality.feature.question.data.QuestionRepositoryImpl
import com.lykimq_uyen.french_nationality.feature.question.domain.repository.QuestionRepository
import com.lykimq_uyen.french_nationality.feature.subcategory.data.SubCategoryRepositoryImpl
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository.SubCategoryRepository

object AppContainer {

    fun categoryRepository(context: Context): CategoryRepository {
        val dao = AppDatabase.getInstance(context).categoryDao()
        return CategoryRepositoryImpl(dao)
    }

    fun subCategoryRepository(context: Context): SubCategoryRepository {
        val dao = AppDatabase.getInstance(context).subCategoryDao()
        return SubCategoryRepositoryImpl(dao)
    }

    fun questionRepository(context: Context): QuestionRepository {
        val dao = AppDatabase.getInstance(context).questionDao()
        return QuestionRepositoryImpl(dao)
    }

    fun studyProgressRepository(context: Context): StudyProgressRepository {
        return StudyProgressRepositoryImpl(context)
    }

    private fun speechPreferencesRepository(context: Context): SpeechPreferencesRepository {
        return SpeechPreferencesRepositoryImpl(context)
    }

    private fun appPreferencesRepository(context: Context): AppPreferencesRepository {
        return AppPreferencesRepositoryImpl(context)
    }

    @Volatile
    private var appThemeController: AppThemeController? = null

    fun appThemeController(context: Context): AppThemeController {
        val existing = appThemeController
        if (existing != null) {
            return existing
        }
        return synchronized(this) {
            appThemeController ?: AppThemeController(
                appPreferencesRepository = appPreferencesRepository(context),
            ).also { appThemeController = it }
        }
    }

    @Volatile
    private var speechController: FrenchSpeechController? = null

    fun frenchSpeechController(context: Context): FrenchSpeechController {
        val existing = speechController
        if (existing != null) {
            return existing
        }
        return synchronized(this) {
            speechController ?: FrenchSpeechController(
                context = context,
                speechPreferencesRepository = speechPreferencesRepository(context),
            ).also { speechController = it }
        }
    }
}
