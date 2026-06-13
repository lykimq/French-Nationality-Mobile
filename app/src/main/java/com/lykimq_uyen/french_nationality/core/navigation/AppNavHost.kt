package com.lykimq_uyen.french_nationality.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lykimq_uyen.french_nationality.feature.home.presentation.HomeScreen
import com.lykimq_uyen.french_nationality.feature.question.presentation.QuestionListScreen
import com.lykimq_uyen.french_nationality.feature.question.presentation.QuestionStudyScreen
import com.lykimq_uyen.french_nationality.feature.settings.presentation.SettingsScreen
import com.lykimq_uyen.french_nationality.feature.subcategory.presentation.SubCategoryListScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(route = AppRoutes.HOME) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(AppRoutes.subCategories(category.id))
                },
                onSettingsClick = {
                    navController.navigate(AppRoutes.SETTINGS)
                },
            )
        }

        composable(route = AppRoutes.SETTINGS) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            route = AppRoutes.SUB_CATEGORIES,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            if (categoryId != null) {
                SubCategoryListScreen(
                    categoryId = categoryId,
                    onBackClick = { navController.popBackStack() },
                    onSubCategoryClick = { subCategory ->
                        navController.navigate(
                            AppRoutes.questions(categoryId, subCategory.id),
                        )
                    },
                )
            }
        }

        composable(
            route = AppRoutes.QUESTIONS,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("subCategoryId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val subCategoryId = backStackEntry.arguments?.getString("subCategoryId")
            if (categoryId != null && subCategoryId != null) {
                QuestionListScreen(
                    categoryId = categoryId,
                    subCategoryId = subCategoryId,
                    onBackClick = { navController.popBackStack() },
                    onQuestionClick = { item ->
                        navController.navigate(
                            AppRoutes.questionStudy(
                                categoryId = categoryId,
                                subCategoryId = subCategoryId,
                                questionId = item.question.id,
                            ),
                        )
                    },
                )
            }
        }

        composable(
            route = AppRoutes.QUESTION_STUDY,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("subCategoryId") { type = NavType.StringType },
                navArgument("questionId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val subCategoryId = backStackEntry.arguments?.getString("subCategoryId")
            val questionId = backStackEntry.arguments?.getString("questionId")
            if (categoryId != null && subCategoryId != null && questionId != null) {
                QuestionStudyScreen(
                    categoryId = categoryId,
                    subCategoryId = subCategoryId,
                    questionId = questionId,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
