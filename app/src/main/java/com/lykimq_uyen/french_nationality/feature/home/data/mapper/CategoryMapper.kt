package com.lykimq_uyen.french_nationality.feature.home.data.mapper

import com.lykimq_uyen.french_nationality.data.db.entity.CategoryEntity
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        title = title,
        description = description.orEmpty(),
        iconKey = icon.orEmpty(),
    )
}

fun List<CategoryEntity>.toCategories(): List<Category> {
    return map { it.toCategory() }
}
