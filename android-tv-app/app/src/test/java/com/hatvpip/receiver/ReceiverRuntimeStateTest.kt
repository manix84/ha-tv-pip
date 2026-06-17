package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class ReceiverRuntimeStateTest {
    @Test
    fun updateStoresPlaybackDiagnostics() {
        ReceiverRuntimeState.update(
            ReceiverPlaybackSnapshot(
                status = PlaybackStatus.Error,
                isPlaying = false,
                mode = ReceiverPlaybackMode.Overlay,
                title = "Front Door",
                url = "https://example.test/stream.m3u8",
                previewUrl = "https://example.test/snapshot.jpg",
                fallbackUrl = "https://example.test/fallback.mjpeg",
                fallbackStreamType = "mjpeg",
                streamType = "hls",
                errorMessage = "decoder failed",
                updatedAtMillis = 1_234L
            )
        )

        val snapshot = ReceiverRuntimeState.snapshot()

        assertEquals("error", snapshot.wirePlaybackState)
        assertFalse(snapshot.isPlaying)
        assertEquals(ReceiverPlaybackMode.Overlay, snapshot.mode)
        assertEquals("Front Door", snapshot.title)
        assertEquals("https://example.test/stream.m3u8", snapshot.url)
        assertEquals("https://example.test/snapshot.jpg", snapshot.previewUrl)
        assertEquals("https://example.test/fallback.mjpeg", snapshot.fallbackUrl)
        assertEquals("mjpeg", snapshot.fallbackStreamType)
        assertEquals("hls", snapshot.streamType)
        assertEquals("decoder failed", snapshot.errorMessage)
        assertEquals(1_234L, snapshot.updatedAtMillis)
    }

    @Test
    fun markIdleClearsPlaybackDiagnostics() {
        ReceiverRuntimeState.update(
            ReceiverPlaybackSnapshot(
                status = PlaybackStatus.Ready,
                isPlaying = true,
                mode = ReceiverPlaybackMode.Overlay,
                title = "Front Door",
                url = "https://example.test/stream.m3u8",
                previewUrl = "https://example.test/snapshot.jpg",
                fallbackUrl = "https://example.test/fallback.mjpeg",
                fallbackStreamType = "mjpeg",
                streamType = "mjpeg",
                updatedAtMillis = 1_234L
            )
        )
        ReceiverRuntimeState.markIdle()

        val snapshot = ReceiverRuntimeState.snapshot()

        assertEquals("idle", snapshot.wirePlaybackState)
        assertNull(snapshot.title)
        assertNull(snapshot.url)
        assertNull(snapshot.previewUrl)
        assertNull(snapshot.fallbackUrl)
        assertNull(snapshot.fallbackStreamType)
        assertNull(snapshot.streamType)
        assertNull(snapshot.errorMessage)
    }
}
