package com.hatvpip.receiver

import android.content.Context
import android.provider.Settings

object ReceiverDeviceInfo {
    fun stableDeviceId(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown"

    fun systemDeviceName(context: Context): String =
        Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
            ?: "Android TV"

    fun deviceName(context: Context): String =
        ReceiverNameSettings.displayName(context)
}
