package com.lykimq_uyen.french_nationality.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_categories")
data class SubCategoryEntity(
    @PrimaryKey val id: String,
    val category_id: String,
    val title: String,
    val sort_order: Int,
)
