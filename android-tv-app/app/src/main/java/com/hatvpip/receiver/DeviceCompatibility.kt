package com.hatvpip.receiver

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings

enum class CompatibilityState(val label: String) {
    Supported("Supported"),
    NotSupported("Not supported"),
    Granted("Granted"),
    NotGranted("Not granted")
}

enum class ReceiverDisplayMode(val label: String) {
    NativePictureInPicture("Native PiP"),
    OverlayFallback("Overlay fallback"),
    FullScreenFallback("Full-screen fallback")
}

data class DeviceCompatibility(
    val androidVersionLabel: String,
    val nativePictureInPicture: CompatibilityState,
    val overlayPermission: CompatibilityState,
    val recommendedMode: ReceiverDisplayMode
) {
    val canRequestOverlayPermission: Boolean
        get() = overlayPermission == CompatibilityState.NotGranted

    val statusText: String
        get() = when (recommendedMode) {
            ReceiverDisplayMode.NativePictureInPicture ->
                "Native Android TV Picture-in-Picture is available."

            ReceiverDisplayMode.OverlayFallback ->
                "Native PiP is not exposed, so the app will use the floating overlay fallback."

            ReceiverDisplayMode.FullScreenFallback ->
                "Native PiP is not exposed. Grant overlay permission to enable the no-ADB floating fallback."
        }
}

object DeviceCompatibilityEvaluator {
    fun from(context: Context): DeviceCompatibility {
        val hasNativePipFeature = context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_PICTURE_IN_PICTURE
        )
        val overlayGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }

        return evaluate(
            sdkInt = Build.VERSION.SDK_INT,
            release = Build.VERSION.RELEASE,
            hasNativePipFeature = hasNativePipFeature,
            overlayGranted = overlayGranted
        )
    }

    fun evaluate(
        sdkInt: Int,
        release: String,
        hasNativePipFeature: Boolean,
        overlayGranted: Boolean
    ): DeviceCompatibility {
        val nativePipSupported = sdkInt >= Build.VERSION_CODES.O && hasNativePipFeature
        val nativePipState = if (nativePipSupported) {
            CompatibilityState.Supported
        } else {
            CompatibilityState.NotSupported
        }
        val overlayState = if (overlayGranted) {
            CompatibilityState.Granted
        } else {
            CompatibilityState.NotGranted
        }
        val recommendedMode = when {
            nativePipSupported -> ReceiverDisplayMode.NativePictureInPicture
            overlayGranted -> ReceiverDisplayMode.OverlayFallback
            else -> ReceiverDisplayMode.FullScreenFallback
        }

        return DeviceCompatibility(
            androidVersionLabel = "Android $release / API $sdkInt",
            nativePictureInPicture = nativePipState,
            overlayPermission = overlayState,
            recommendedMode = recommendedMode
        )
    }
}
