package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ControlRuntimeStateTest {
    @Test
    fun markStartedRecordsRunningPortAndUptime() {
        ControlRuntimeState.markStarted(port = 8765, nowMillis = 1_000L)

        val snapshot = ControlRuntimeState.snapshot()

        assertTrue(snapshot.running)
        assertEquals(8765, snapshot.port)
        assertEquals(4L, snapshot.uptimeSeconds(nowMillis = 5_500L))
    }

    @Test
    fun recordRequestIncrementsCountAndStoresLastRequest() {
        ControlRuntimeState.markStarted(port = 8765, nowMillis = 1_000L)
        ControlRuntimeState.recordRequest(
            method = "POST",
            path = "/show",
            status = 202,
            nowMillis = 2_000L
        )

        val snapshot = ControlRuntimeState.snapshot()

        assertEquals(1L, snapshot.requestCount)
        assertEquals("POST", snapshot.lastRequest?.method)
        assertEquals("/show", snapshot.lastRequest?.path)
        assertEquals(202, snapshot.lastRequest?.status)
    }

    @Test
    fun markStoppedKeepsDiagnosticsButClearsRunningFlag() {
        ControlRuntimeState.markStarted(port = 8765, nowMillis = 1_000L)
        ControlRuntimeState.recordRequest("GET", "/status", 200, nowMillis = 2_000L)
        ControlRuntimeState.markStopped()

        val snapshot = ControlRuntimeState.snapshot()

        assertFalse(snapshot.running)
        assertEquals(1L, snapshot.requestCount)
        assertEquals("/status", snapshot.lastRequest?.path)
    }
}
