package com.hatvpip.receiver

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun CompatibilityState.localizedLabel(): String = when (this) {
    CompatibilityState.Supported -> stringResource(R.string.compatibility_supported)
    CompatibilityState.NotSupported -> stringResource(R.string.compatibility_not_supported)
    CompatibilityState.Granted -> stringResource(R.string.compatibility_granted)
    CompatibilityState.NotGranted -> stringResource(R.string.compatibility_not_granted)
}

@Composable
fun ReceiverDisplayMode.localizedLabel(): String = when (this) {
    ReceiverDisplayMode.NativePictureInPicture -> stringResource(R.string.display_mode_native_pip)
    ReceiverDisplayMode.OverlayFallback -> stringResource(R.string.display_mode_overlay_fallback)
    ReceiverDisplayMode.FullScreenFallback -> stringResource(R.string.display_mode_full_screen_fallback)
}

@Composable
fun DeviceCompatibility.localizedStatusText(): String = when (recommendedMode) {
    ReceiverDisplayMode.NativePictureInPicture -> stringResource(R.string.compatibility_native_pip_available)
    ReceiverDisplayMode.OverlayFallback -> stringResource(R.string.compatibility_overlay_fallback_available)
    ReceiverDisplayMode.FullScreenFallback -> stringResource(R.string.compatibility_overlay_permission_needed)
}

@Composable
fun PlaybackStatus.localizedLabel(): String = when (this) {
    PlaybackStatus.Idle -> stringResource(R.string.playback_idle)
    PlaybackStatus.Buffering -> stringResource(R.string.playback_buffering)
    PlaybackStatus.Ready -> stringResource(R.string.playback_ready)
    PlaybackStatus.Ended -> stringResource(R.string.playback_ended)
    PlaybackStatus.Error -> stringResource(R.string.playback_error)
}

@Composable
fun PlayerPlaybackState.localizedDisplayText(): String {
    val playingState = if (isPlaying) {
        stringResource(R.string.playback_playing)
    } else {
        stringResource(R.string.playback_paused)
    }
    val base = stringResource(R.string.playback_state_value, status.localizedLabel(), playingState)
    return errorMessage?.let { "$base - $it" } ?: base
}
