package com.example.tmdb.ui.screen.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.tmdb.ui.screen.favoriteMovie.FavoriteMovieViewModel
import com.example.tmdb.ui.screen.movie.MOVIE_SCREEN_KEY_ID
import com.example.tmdb.ui.screen.movie.movieScreenRoute
import com.example.tmdb.ui.screen.movieDetail.generateMovieDetailScreenRoute
import com.example.tmdb.ui.screen.movieDetail.movieDetailScreenRoute
import com.example.tmdb.ui.theme.TMDBTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.initialize(this)
        setContent {
            val sessionState by mainViewModel.sessionState.collectAsState()

            TMDBTheme {
                when (sessionState) {
                    is SessionState.Uninitialized -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text("未取得TMDB request_token授權")
                            Button(
                                onClick = {mainViewModel.retryAuthorization(this@MainActivity) }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                    is SessionState.Initialized -> {
                        Scaffold(
                            modifier = Modifier.fillMaxSize()
                        ) { innerPadding ->
                            TmdbNavHost(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }

        val uri: Uri? = intent.data
        if (uri != null && uri.scheme == "com.example.tmdb") {
            mainViewModel.handleAuthCallback(uri)
        } else {
            mainViewModel.initializeSessionIfNeeded(this)
        }
    }
}

@Composable
fun TmdbNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    favoriteMovieViewModel: FavoriteMovieViewModel = viewModel()
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MOVIE_SCREEN_KEY_ID
    ) {
        movieScreenRoute(navigateToMoviesDetail = {
            navController.navigate(
                generateMovieDetailScreenRoute(it)
            )
        }, favoriteMovieViewModel = favoriteMovieViewModel)
        movieDetailScreenRoute(navController, favoriteMovieViewModel = favoriteMovieViewModel)
    }

}



