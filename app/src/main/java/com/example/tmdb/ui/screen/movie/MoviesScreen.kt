package com.example.tmdb.ui.screen.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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

    val isLoading by favoriteMovieViewModel.isLoading.collectAsState()
    val moviesPagingItems = moviesViewModel.moviePagingFlow.collectAsLazyPagingItems()
    MoviesScreen(
        isLoading = isLoading,
        moviesPagingItems = moviesPagingItems,
        onItemClick = onItemClick,
        favoriteMovieViewModel = favoriteMovieViewModel,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    isLoading: Boolean = true,
    moviesPagingItems: LazyPagingItems<MoviesDetails> ,
    onItemClick: (Int) -> Unit = {},
    favoriteMovieViewModel: FavoriteMovieViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.movieList)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                moviesPagingItems.loadState.refresh is LoadState.Error-> {
                    Text(
                        text = (moviesPagingItems.loadState.refresh as LoadState.Error).error.message.orEmpty(),
                        modifier = Modifier
                            .padding(20.dp)
                    )
                }
                isLoading || moviesPagingItems.loadState.refresh is LoadState.Loading-> {
                    CircularProgressIndicator()
                }
                else -> {
                    MovieList(moviesPagingItems, onItemClick, favoriteMovieViewModel)
                }
            }
        }
    }
}

@Composable
fun MovieList(
    moviesPagingItems: LazyPagingItems<MoviesDetails>,
    onItemClick: (Int) -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(moviesPagingItems.itemCount) { index ->
            val movie = moviesPagingItems[index]
            movie?.let {
                MovieItem(
                    modifier = Modifier.fillMaxWidth(),
                    movie = it,
                    onItemClick = onItemClick,
                    favoriteMovieViewModel = favoriteMovieViewModel
                )
            }
        }

        moviesPagingItems.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    item {
                        Text(
                            text = "加載錯誤，請重試",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
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
    var isFavor by remember { mutableStateOf(false) }
    val favoriteList by favoriteMovieViewModel.favoriteList.collectAsState()
    LaunchedEffect(Unit, favoriteList.size) {
        isFavor = favoriteMovieViewModel.isMovieFavorited(movie.id)
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
                    val newFavorState = !isFavor
                    favoriteMovieViewModel.toggleFavoriteMovie(movie, newFavorState)
                    isFavor = newFavorState
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFavor) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite),
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(4.dp)
                .heightIn(min = 48.dp),
        )
    }
}


