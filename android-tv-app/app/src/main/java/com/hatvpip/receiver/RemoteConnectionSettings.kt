package com.hatvpip.receiver

import android.content.Context

data class RemoteConnectionConfig(
    val homeAssistantUrl: String,
    val accessToken: String
) {
    val enabled: Boolean
        get() = homeAssistantUrl.isNotBlank() && accessToken.isNotBlank()
}

object RemoteConnectionSettings {
    private const val PREFS_NAME = "ha_tv_pip_remote_connection"
    private const val KEY_HOME_ASSISTANT_URL = "home_assistant_url"
    private const val KEY_ACCESS_TOKEN = "access_token"

    fun load(context: Context): RemoteConnectionConfig {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return RemoteConnectionConfig(
            homeAssistantUrl = prefs.getString(KEY_HOME_ASSISTANT_URL, "") ?: "",
            accessToken = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""
        )
    }

    fun save(context: Context, config: RemoteConnectionConfig) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_HOME_ASSISTANT_URL, config.homeAssistantUrl.trim())
            .putString(KEY_ACCESS_TOKEN, config.accessToken.trim())
            .apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
