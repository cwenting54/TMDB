package com.example.tmdb.ui.screen.base

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "session_preferences"
    private const val KEY_SESSION_ID = "session_id"
    private const val KEY_ACCOUNT_ID = "account_id"

    private lateinit var sharedPreferences: SharedPreferences

    var sessionId: String = ""
        private set
    var accountId: Int = 0
        private set

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sessionId = sharedPreferences.getString(KEY_SESSION_ID, "") ?: ""
        accountId = sharedPreferences.getInt(KEY_ACCOUNT_ID, 0)
    }

    fun saveSessionData(sessionId: String, accountId: Int) {
        SessionManager.sessionId = sessionId
        SessionManager.accountId = accountId
        sharedPreferences.edit()
            .putString(KEY_SESSION_ID, sessionId)
            .putInt(KEY_ACCOUNT_ID, accountId)
            .apply()
    }

    fun isSessionAvailable(): Boolean {
        return sessionId.isNotEmpty() && accountId != 0
    }
}
