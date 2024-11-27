package com.example.tmdb.ui.screen.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.model.RequestToken
import com.example.tmdb.network.ApiInterface
import com.example.tmdb.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: ApiInterface = ApiService.create(ApiInterface::class.java)) :
    ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Uninitialized)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private var currentRequestToken: String? = null

    fun initializeSessionIfNeeded(context: Context) {
        viewModelScope.launch {
            if (SessionManager.isSessionAvailable()) {
                _sessionState.value = SessionState.Initialized
                return@launch
            }

            try {
                val requestToken = fetchRequestToken(apiService)
                if (requestToken != null) {
                    currentRequestToken = requestToken
                    val authUrl =
                        "https://www.themoviedb.org/authenticate/$requestToken?redirect_to=com.example.tmdb://approved"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    Log.e("sessionInitial", "Failed to fetch requestToken")
                }
            } catch (e: Exception) {
                Log.e("sessionInitial", "Error initializing session: ${e.message}")
            }
        }
    }


    fun handleAuthCallback(uri: Uri) {
        viewModelScope.launch {
            val requestToken = uri.getQueryParameter("request_token")
            val approved = uri.getQueryParameter("approved")

            if (approved == "true" && requestToken != null) {
                createSessionAndSave(requestToken)
            } else {
                Log.e("sessionInitial", "Authorization failed or user denied access")
            }
        }
    }


    private suspend fun fetchRequestToken(apiService: ApiInterface): String? {
        return try {
            val response = apiService.createRequestToken()
            response.requestToken
        } catch (e: Exception) {
            Log.e("sessionInitial", "Error fetching request token: ${e.message}")
            null
        }
    }

    private fun createSessionAndSave(requestToken: String) {
        viewModelScope.launch {
            try {
                val sessionResponse = apiService.createSession(RequestToken(requestToken))
                val sessionId = sessionResponse.sessionId
                val accountResponse = apiService.getAccountDetails(sessionId)
                val accountId = accountResponse.id

                SessionManager.saveSessionData(sessionId, accountId)

                _sessionState.value = SessionState.Initialized
            } catch (e: Exception) {
                Log.e("sessionInitial", "Error during session creation: ${e.message}")
            }
        }
    }

    fun retryAuthorization(context: Context) {
        _sessionState.value = SessionState.Uninitialized
        initializeSessionIfNeeded(context)
    }
}


sealed class SessionState {
    object Uninitialized : SessionState()
    object Initialized : SessionState()
}