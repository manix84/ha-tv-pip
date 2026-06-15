package com.hatvpip.receiver

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class OverlayPlayerService : Service() {
    private lateinit var windowManager: WindowManager
    private var overlayView: FrameLayout? = null
    private var player: ExoPlayer? = null
    private var title: String = "HA TV PiP"
    private var url: String = PlayerActivity.TEST_STREAM_URL
    private var durationSeconds: Int? = null
    private val autoCloseHandler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> stopSelf()
            else -> {
                title = intent?.getStringExtra(PlayerActivity.EXTRA_TITLE) ?: title
                url = intent?.getStringExtra(PlayerActivity.EXTRA_URL) ?: url
                durationSeconds = intent?.takeIf {
                    it.hasExtra(PlayerActivity.EXTRA_DURATION_SECONDS)
                }?.getIntExtra(PlayerActivity.EXTRA_DURATION_SECONDS, 0)?.takeIf { it > 0 }
                showOverlay()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        autoCloseHandler.removeCallbacksAndMessages(null)
        removeOverlay()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun showOverlay() {
        if (overlayView != null) {
            removeOverlay()
        }

        val overlayPlayer = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }
        player = overlayPlayer

        val playerView = PlayerView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            useController = false
            this.player = overlayPlayer
        }

        val root = FrameLayout(this).apply {
            setBackgroundColor(android.graphics.Color.BLACK)
            addView(playerView)
        }

        val params = WindowManager.LayoutParams(
            OVERLAY_WIDTH_PX,
            OVERLAY_HEIGHT_PX,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = OVERLAY_MARGIN_PX
            y = OVERLAY_MARGIN_PX
        }

        runCatching {
            windowManager.addView(root, params)
            overlayView = root
            ReceiverRuntimeState.update(
                ReceiverPlaybackSnapshot(
                    status = PlaybackStatus.Ready,
                    isPlaying = true,
                    mode = ReceiverPlaybackMode.Overlay,
                    title = title,
                    url = url
                )
            )
            AppLog.playbackStart(url)
            scheduleAutoClose()
        }.onFailure { error ->
            AppLog.error("Unable to show overlay fallback", error)
            overlayPlayer.release()
            player = null
            stopSelf()
        }
    }

    private fun removeOverlay() {
        overlayView?.let { view ->
            runCatching { windowManager.removeView(view) }
        }
        overlayView = null
        player?.release()
        player = null
        ReceiverRuntimeState.markIdle()
        AppLog.playbackStop(reason = "overlay_service_stopped")
    }

    private fun scheduleAutoClose() {
        autoCloseHandler.removeCallbacksAndMessages(null)
        val duration = durationSeconds ?: return
        autoCloseHandler.postDelayed({ stopSelf() }, duration * 1_000L)
    }

    companion object {
        const val ACTION_SHOW = "com.hatvpip.receiver.action.SHOW_OVERLAY"
        const val ACTION_STOP = "com.hatvpip.receiver.action.STOP_OVERLAY"
        private const val OVERLAY_WIDTH_PX = 640
        private const val OVERLAY_HEIGHT_PX = 360
        private const val OVERLAY_MARGIN_PX = 48
    }
}
