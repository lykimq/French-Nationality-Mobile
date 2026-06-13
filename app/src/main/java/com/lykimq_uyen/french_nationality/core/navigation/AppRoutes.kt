package com.lykimq_uyen.french_nationality.core.navigation

object AppRoutes {
    const val HOME = "home"
    const val SUB_CATEGORIES = "sub_categories/{categoryId}"
    const val QUESTIONS = "questions/{categoryId}/{subCategoryId}"
    const val QUESTION_STUDY = "study/{categoryId}/{subCategoryId}/{questionId}"

    fun subCategories(categoryId: String): String = "sub_categories/$categoryId"

    fun questions(categoryId: String, subCategoryId: String): String {
        return "questions/$categoryId/$subCategoryId"
    }

    fun questionStudy(
        categoryId: String,
        subCategoryId: String,
        questionId: String,
    ): String {
        return "study/$categoryId/$subCategoryId/$questionId"
    }
}
