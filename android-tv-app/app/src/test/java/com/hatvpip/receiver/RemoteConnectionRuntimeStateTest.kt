package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RemoteConnectionRuntimeStateTest {
    @Before
    fun resetState() {
        RemoteConnectionRuntimeState.resetForTest()
    }

    @Test
    fun markConnectingTracksAttemptsAndClearsPreviousError() {
        RemoteConnectionRuntimeState.markDisconnected("connection_failed", nowMillis = 500L)
        RemoteConnectionRuntimeState.markConnecting("https://ha.example", nowMillis = 1_000L)

        val snapshot = RemoteConnectionRuntimeState.snapshot()

        assertEquals(RemoteConnectionStatus.Connecting, snapshot.status)
        assertEquals("https://ha.example", snapshot.homeAssistantUrl)
        assertEquals(1L, snapshot.connectionAttemptCount)
        assertEquals(1_000L, snapshot.lastConnectionAttemptAtMillis)
        assertNull(snapshot.lastError)
    }

    @Test
    fun markConnectedTracksSuccessfulConnectionsWithoutResettingAttempts() {
        RemoteConnectionRuntimeState.markConnecting("https://ha.example", nowMillis = 1_000L)
        RemoteConnectionRuntimeState.markConnected("https://ha.example", nowMillis = 2_000L)

        val snapshot = RemoteConnectionRuntimeState.snapshot()

        assertEquals(RemoteConnectionStatus.Connected, snapshot.status)
        assertEquals(1L, snapshot.connectionAttemptCount)
        assertEquals(1L, snapshot.successfulConnectionCount)
        assertEquals(2_000L, snapshot.connectedAtMillis)
        assertEquals(2_000L, snapshot.lastMessageAtMillis)
    }

    @Test
    fun markMessageTracksLastMessageAndCount() {
        RemoteConnectionRuntimeState.markConnected("https://ha.example", nowMillis = 1_000L)
        RemoteConnectionRuntimeState.markMessage(nowMillis = 2_000L)
        RemoteConnectionRuntimeState.markMessage(nowMillis = 3_000L)

        val snapshot = RemoteConnectionRuntimeState.snapshot()

        assertEquals(2L, snapshot.messageCount)
        assertEquals(3_000L, snapshot.lastMessageAtMillis)
    }

    @Test
    fun markDisconnectedKeepsCountersAndTracksReason() {
        RemoteConnectionRuntimeState.markConnecting("https://ha.example", nowMillis = 1_000L)
        RemoteConnectionRuntimeState.markConnected("https://ha.example", nowMillis = 2_000L)
        RemoteConnectionRuntimeState.markMessage(nowMillis = 3_000L)
        RemoteConnectionRuntimeState.markDisconnected("auth_invalid", nowMillis = 4_000L)

        val snapshot = RemoteConnectionRuntimeState.snapshot()

        assertEquals(RemoteConnectionStatus.Error, snapshot.status)
        assertEquals(1L, snapshot.connectionAttemptCount)
        assertEquals(1L, snapshot.successfulConnectionCount)
        assertEquals(1L, snapshot.messageCount)
        assertEquals("auth_invalid", snapshot.lastError)
        assertEquals("auth_invalid", snapshot.lastDisconnectReason)
        assertEquals(4_000L, snapshot.lastDisconnectedAtMillis)
    }
}
