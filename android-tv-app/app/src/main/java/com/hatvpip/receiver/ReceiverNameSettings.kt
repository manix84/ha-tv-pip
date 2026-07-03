package com.hatvpip.receiver

import android.content.Context

object ReceiverNameSettings {
    private const val PREFS_NAME = "receiver_name_settings"
    private const val KEY_CUSTOM_NAME = "custom_name"
    private const val MAX_NAME_LENGTH = 64

    fun customName(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CUSTOM_NAME, null)
            ?.sanitizeReceiverName()
            ?.takeIf { it.isNotBlank() }

    fun displayName(context: Context): String =
        customName(context) ?: ReceiverDeviceInfo.systemDeviceName(context)

    fun save(context: Context, name: String): String {
        val sanitized = name.sanitizeReceiverName()
        require(sanitized.isNotBlank()) { "Receiver name cannot be blank" }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CUSTOM_NAME, sanitized)
            .apply()
        return sanitized
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_CUSTOM_NAME)
            .apply()
    }

    internal fun sanitizeNameForTest(name: String): String = name.sanitizeReceiverName()

    private fun String.sanitizeReceiverName(): String =
        trim()
            .replace(Regex("\\s+"), " ")
            .take(MAX_NAME_LENGTH)
            .trim()
}
