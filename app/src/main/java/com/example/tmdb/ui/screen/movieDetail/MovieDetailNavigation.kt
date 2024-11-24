package com.example.tmdb.ui.screen.movieDetail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val MOVIE_DETAIL_SCREEN_KEY_ID = "movieDetail"

const val MOVIE_DETAIL_SCREEN_ARGS_KEY = "movieId"

val MOVIE_DETAIL_SCREEN_ROUTE =
    "$MOVIE_DETAIL_SCREEN_KEY_ID?$MOVIE_DETAIL_SCREEN_ARGS_KEY={$MOVIE_DETAIL_SCREEN_ARGS_KEY}"

fun generateMovieDetailScreenRoute(movieId: Int): String {
    return "$MOVIE_DETAIL_SCREEN_KEY_ID?$MOVIE_DETAIL_SCREEN_ARGS_KEY=$movieId"
}

fun NavGraphBuilder.movieDetailScreenRoute(navController: NavHostController) {
    composable(
        route = MOVIE_DETAIL_SCREEN_ROUTE,
        arguments = listOf(
            navArgument(MOVIE_DETAIL_SCREEN_ARGS_KEY) { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getInt(MOVIE_DETAIL_SCREEN_ARGS_KEY) ?: 0
        MovieDetailRoute(movieId = movieId, navController = navController)
    }
}