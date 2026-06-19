package com.hatvpip.receiver

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector

@OptIn(UnstableApi::class)
fun buildReceiverPlayer(context: Context): ExoPlayer {
    val renderersFactory = DefaultRenderersFactory(context)
        .setEnableDecoderFallback(true)
    val trackSelector = DefaultTrackSelector(context).apply {
        parameters = buildUponParameters()
            .setMaxVideoSize(RECEIVER_MAX_VIDEO_WIDTH, RECEIVER_MAX_VIDEO_HEIGHT)
            .setMaxVideoBitrate(RECEIVER_MAX_VIDEO_BITRATE)
            .build()
    }

    return ExoPlayer.Builder(context, renderersFactory)
        .setTrackSelector(trackSelector)
        .build()
}

private const val RECEIVER_MAX_VIDEO_WIDTH = 1280
private const val RECEIVER_MAX_VIDEO_HEIGHT = 720
private const val RECEIVER_MAX_VIDEO_BITRATE = 2_500_000
