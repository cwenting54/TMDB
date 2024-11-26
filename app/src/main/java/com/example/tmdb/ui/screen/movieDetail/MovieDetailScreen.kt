package com.example.tmdb.ui.screen.movieDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tmdb.R
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.network.ImageApi
import com.example.tmdb.ui.screen.favoriteMovie.FavoriteMovieViewModel


@Composable
fun MovieDetailRoute(
    movieDetailViewModel: MovieDetailViewModel = viewModel(),
    favoriteMovieViewModel: FavoriteMovieViewModel,
    movieId: Int,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        movieDetailViewModel.fetchMovieDetail(movieId)
    }
    val moviesDetail by movieDetailViewModel.moviesDetail.collectAsState()
    MovieDetailScreen(
        moviesDetail,
        movieId = movieId,
        navController = navController,
        favoriteMovieViewModel = favoriteMovieViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    moviesDetail: MoviesDetails?,
    movieId: Int,
    navController: NavHostController,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    var isFavor by remember { mutableStateOf(false) }
    val favoriteList by favoriteMovieViewModel.favoriteList.collectAsState()
    LaunchedEffect(movieId, favoriteList.size) {
        isFavor = favoriteMovieViewModel.isMovieFavorited(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.movieDetail)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_ios_new_24),
                            contentDescription = stringResource(R.string.backPage)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        moviesDetail?.let { movie ->
                            val newFavorState = !isFavor
                            favoriteMovieViewModel.toggleFavoriteMovie(movie, newFavorState)
                            isFavor = newFavorState
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavor == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.favorite),
                            tint = Color.Red,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = ImageApi.getImageUrl(moviesDetail?.backdropPath),
                contentDescription = stringResource(R.string.moviePhoto),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = moviesDetail?.title ?: "",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "總長: ${moviesDetail?.runtime ?: 0.0}分鐘")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "評分: ${moviesDetail?.voteAverage ?: 0.0}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "發行日期: ${moviesDetail?.releaseDate ?: ""}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = moviesDetail?.overview ?: "",
            )
        }
    }
}