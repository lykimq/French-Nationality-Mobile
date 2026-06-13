package com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository

import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory

interface SubCategoryRepository {
    suspend fun getSubCategories(categoryId: String): List<SubCategory>

    suspend fun getSubCategoryById(subCategoryId: String): SubCategory?
}
