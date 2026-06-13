package com.lykimq_uyen.french_nationality.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_meta")
data class DbMetaEntity(
    @PrimaryKey val key: String,
    val value: String,
)
