package com.lykimq_uyen.french_nationality.feature.home.domain.repository

import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>

    suspend fun getCategoryById(categoryId: String): Category?
}
