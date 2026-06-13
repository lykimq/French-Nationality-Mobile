package com.lykimq_uyen.french_nationality.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lykimq_uyen.french_nationality.data.db.entity.DbMetaEntity

@Dao
interface DbMetaDao {
    @Query("SELECT * FROM db_meta WHERE `key` = :key LIMIT 1")
    suspend fun getMeta(key: String): DbMetaEntity?
}
