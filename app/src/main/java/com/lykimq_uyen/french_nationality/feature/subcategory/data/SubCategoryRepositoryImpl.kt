package com.lykimq_uyen.french_nationality.feature.subcategory.data

import com.lykimq_uyen.french_nationality.data.db.dao.SubCategoryDao
import com.lykimq_uyen.french_nationality.feature.subcategory.data.mapper.toSubCategory
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository.SubCategoryRepository

class SubCategoryRepositoryImpl(
    private val subCategoryDao: SubCategoryDao,
) : SubCategoryRepository {

    override suspend fun getSubCategories(categoryId: String): List<SubCategory> {
        return subCategoryDao.getByCategoryId(categoryId).map { entity ->
            entity.toSubCategory(
                questionCount = subCategoryDao.getQuestionCount(entity.id),
            )
        }
    }

    override suspend fun getSubCategoryById(subCategoryId: String): SubCategory? {
        val entity = subCategoryDao.getById(subCategoryId) ?: return null
        return entity.toSubCategory(
            questionCount = subCategoryDao.getQuestionCount(entity.id),
        )
    }
}
