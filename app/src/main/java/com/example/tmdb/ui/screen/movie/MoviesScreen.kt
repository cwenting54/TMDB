package com.example.tmdb.ui.screen.movie

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tmdb.BuildConfig
import com.example.tmdb.R
import com.example.tmdb.model.MoviesDetails


@Composable
fun MoviesRoute(
    moviesViewModel: MoviesViewModel = viewModel(),
    onItemClick: (MoviesDetails) -> Unit = {}
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
        onItemClick = onItemClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    errorMessage: String?,
    moviesResponse: List<MoviesDetails>? = listOf(),
    onItemClick: (MoviesDetails) -> Unit = {}
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
                    MovieList(movies)
                } ?: run {
                    Text(text = "No movies available", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<MoviesDetails>) {
    LazyColumn {
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}

@Composable
fun MovieItem(movie: MoviesDetails) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val imageUrl = "${BuildConfig.IMAGE_URL}/w200${movie.posterPath}"
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.moviePhoto),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )
        Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = { /* TODO: 添加點擊事件 */ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.favorite),
                tint = Color.Red
            )
        }
    }
}


