package com.lykimq_uyen.french_nationality.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lykimq_uyen.french_nationality.data.db.dao.CategoryDao
import com.lykimq_uyen.french_nationality.data.db.dao.QuestionDao
import com.lykimq_uyen.french_nationality.data.db.dao.SubCategoryDao
import com.lykimq_uyen.french_nationality.data.db.entity.CategoryEntity
import com.lykimq_uyen.french_nationality.data.db.entity.DbMetaEntity
import com.lykimq_uyen.french_nationality.data.db.entity.QuestionEntity
import com.lykimq_uyen.french_nationality.data.db.entity.SubCategoryEntity

@Database(
    entities = [
        CategoryEntity::class,
        SubCategoryEntity::class,
        QuestionEntity::class,
        DbMetaEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun questionDao(): QuestionDao

    companion object {
        private const val DB_NAME = "french_questions.db"
        private const val ASSET_PATH = "database/french_questions.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context.applicationContext).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .createFromAsset(ASSET_PATH)
                .build()
        }
    }
}
