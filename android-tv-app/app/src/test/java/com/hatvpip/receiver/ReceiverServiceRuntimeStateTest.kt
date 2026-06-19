package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReceiverServiceRuntimeStateTest {
    @Before
    fun resetState() {
        ReceiverServiceRuntimeState.resetForTest()
    }

    @Test
    fun markBootReceiverActionStoresLastAction() {
        ReceiverServiceRuntimeState.markBootReceiverAction(
            action = "android.intent.action.BOOT_COMPLETED",
            nowMillis = 1_000L
        )

        val snapshot = ReceiverServiceRuntimeState.snapshot()

        assertEquals("android.intent.action.BOOT_COMPLETED", snapshot.lastBootReceiverAction)
        assertEquals(1_000L, snapshot.lastBootReceiverAtMillis)
    }

    @Test
    fun markForegroundStartedTracksForegroundState() {
        ReceiverServiceRuntimeState.markForegroundStarted()

        assertTrue(ReceiverServiceRuntimeState.snapshot().foreground)
    }

    @Test
    fun markServiceStartedRecordsReasonTimeAndCount() {
        ReceiverServiceRuntimeState.markServiceStarted("requested", nowMillis = 1_000L)
        ReceiverServiceRuntimeState.markServiceStarted("package_replaced", nowMillis = 2_000L)

        val snapshot = ReceiverServiceRuntimeState.snapshot()

        assertTrue(snapshot.running)
        assertEquals(2L, snapshot.startCount)
        assertEquals("package_replaced", snapshot.lastStartReason)
        assertEquals(2_000L, snapshot.lastStartedAtMillis)
        assertNull(snapshot.lastDestroyedAtMillis)
    }

    @Test
    fun markServiceDestroyedKeepsDiagnosticsButClearsRunningFlags() {
        ReceiverServiceRuntimeState.markForegroundStarted()
        ReceiverServiceRuntimeState.markServiceStarted("requested", nowMillis = 1_000L)
        ReceiverServiceRuntimeState.markServiceDestroyed(nowMillis = 3_000L)

        val snapshot = ReceiverServiceRuntimeState.snapshot()

        assertFalse(snapshot.running)
        assertFalse(snapshot.foreground)
        assertEquals(1L, snapshot.startCount)
        assertEquals("requested", snapshot.lastStartReason)
        assertEquals(3_000L, snapshot.lastDestroyedAtMillis)
    }
}
