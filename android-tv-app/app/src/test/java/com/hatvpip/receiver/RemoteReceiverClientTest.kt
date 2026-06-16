package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteReceiverClientTest {
    @Test
    fun `websocket url uses secure websocket for https Home Assistant URL`() {
        assertEquals(
            "wss://home.example.test/api/websocket",
            RemoteReceiverClient.homeAssistantWebSocketUrl("https://home.example.test/")
        )
    }

    @Test
    fun `websocket url uses plain websocket for http Home Assistant URL`() {
        assertEquals(
            "ws://10.0.0.2:8123/api/websocket",
            RemoteReceiverClient.homeAssistantWebSocketUrl("http://10.0.0.2:8123")
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `websocket url rejects unsupported schemes`() {
        RemoteReceiverClient.homeAssistantWebSocketUrl("ftp://home.example.test")
    }

    @Test
    fun `remote config is enabled only when url and token are present`() {
        assertTrue(
            RemoteConnectionConfig(
                homeAssistantUrl = "https://home.example.test",
                accessToken = "token"
            ).enabled
        )
        assertFalse(
            RemoteConnectionConfig(
                homeAssistantUrl = "https://home.example.test",
                accessToken = ""
            ).enabled
        )
    }
}
