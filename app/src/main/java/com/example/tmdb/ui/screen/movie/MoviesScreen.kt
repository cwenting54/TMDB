package com.example.tmdb.ui.screen.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tmdb.R
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.network.ImageApi
import com.example.tmdb.ui.screen.favoriteMovie.FavoriteMovieViewModel


@Composable
fun MoviesRoute(
    moviesViewModel: MoviesViewModel = viewModel(),
    favoriteMovieViewModel: FavoriteMovieViewModel,
    onItemClick: (Int) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        moviesViewModel.fetchMovies()
    }
    val isLoading by moviesViewModel.isLoading.collectAsState()
    val moviesResponse by moviesViewModel.moviesList.collectAsState()
    val errorMessage by moviesViewModel.errorMessage.collectAsState()

    MoviesScreen(
        isLoading = isLoading,
        moviesResponse = moviesResponse?.results,
        errorMessage = errorMessage,
        onItemClick = onItemClick,
        favoriteMovieViewModel = favoriteMovieViewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    isLoading: Boolean = true,
    errorMessage: String?,
    moviesResponse: List<MoviesDetails>? = listOf(),
    onItemClick: (Int) -> Unit = {},
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.movieList)) })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Text(text = "Error: $errorMessage", modifier = Modifier.align(Alignment.Center))
            } else {
                moviesResponse?.let { movies ->
                    MovieList(movies, onItemClick, favoriteMovieViewModel)
                } ?: run {
                    Text(text = "No movies available", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieList(
    movies: List<MoviesDetails>,
    onItemClick: (Int) -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(movies.chunked(2)) { moviePair ->
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                moviePair.forEach { movie ->
                    MovieItem(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        movie = movie,
                        onItemClick = onItemClick,
                        favoriteMovieViewModel = favoriteMovieViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    modifier: Modifier,
    movie: MoviesDetails,
    onItemClick: (Int) -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    val isFavor = remember { mutableStateOf(favoriteMovieViewModel.isMovieFavorited(movie.id)) }

    LaunchedEffect(movie.id) {
        isFavor.value = favoriteMovieViewModel.isMovieFavorited(movie.id)
    }

    Card(
        modifier = modifier
            .clickable { onItemClick(movie.id) }
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
        ) {
        Box {
            AsyncImage(
                model = ImageApi.getImageUrl(movie.posterPath),
                contentDescription = stringResource(R.string.moviePhoto),
                modifier = Modifier
                    .fillMaxSize()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = {
                    val newFavorState = !isFavor.value
                    favoriteMovieViewModel.toggleFavoriteMovie(movie.id, newFavorState)
                    isFavor.value = newFavorState
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top=8.dp)
            ) {
                Icon(
                    imageVector = if (isFavor.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite),
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)
        )
    }
}


