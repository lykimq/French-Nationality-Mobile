package com.lykimq_uyen.french_nationality.feature.subcategory.data.mapper

import com.lykimq_uyen.french_nationality.data.db.entity.SubCategoryEntity
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory

fun SubCategoryEntity.toSubCategory(questionCount: Int): SubCategory {
    return SubCategory(
        id = id,
        categoryId = category_id,
        title = title,
        questionCount = questionCount,
    )
}
