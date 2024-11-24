package com.example.tmdb.ui.screen.movie

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tmdb.ui.screen.favoriteMovie.FavoriteMovieViewModel

const val MOVIE_SCREEN_KEY_ID = "movies"

fun NavGraphBuilder.movieScreenRoute(
    navigateToMoviesDetail: (Int) -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    composable(route = MOVIE_SCREEN_KEY_ID) {
        MoviesRoute(onItemClick = navigateToMoviesDetail,favoriteMovieViewModel = favoriteMovieViewModel)
    }
}