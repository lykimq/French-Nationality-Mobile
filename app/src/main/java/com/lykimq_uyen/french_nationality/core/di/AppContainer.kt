package com.lykimq_uyen.french_nationality.core.di

import android.content.Context
import com.lykimq_uyen.french_nationality.data.db.AppDatabase
import com.lykimq_uyen.french_nationality.feature.home.data.CategoryRepositoryImpl
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository

object AppContainer {

    fun categoryRepository(context: Context): CategoryRepository {
        val dao = AppDatabase.getInstance(context).categoryDao()
        return CategoryRepositoryImpl(dao)
    }
}
