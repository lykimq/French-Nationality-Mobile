package com.lykimq_uyen.french_nationality.feature.home.data

import com.lykimq_uyen.french_nationality.data.db.dao.CategoryDao
import com.lykimq_uyen.french_nationality.feature.home.data.mapper.toCategories
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return categoryDao.getAllCategories().toCategories()
    }
}
