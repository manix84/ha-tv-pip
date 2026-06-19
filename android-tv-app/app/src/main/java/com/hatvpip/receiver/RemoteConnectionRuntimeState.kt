package com.hatvpip.receiver

import java.util.concurrent.atomic.AtomicReference

enum class RemoteConnectionStatus(val wireName: String) {
    Disabled("disabled"),
    Connecting("connecting"),
    Connected("connected"),
    Disconnected("disconnected"),
    Error("error")
}

data class RemoteConnectionSnapshot(
    val status: RemoteConnectionStatus = RemoteConnectionStatus.Disabled,
    val homeAssistantUrl: String? = null,
    val lastError: String? = null,
    val connectedAtMillis: Long? = null,
    val lastMessageAtMillis: Long? = null,
    val connectionAttemptCount: Long = 0,
    val successfulConnectionCount: Long = 0,
    val messageCount: Long = 0,
    val lastConnectionAttemptAtMillis: Long? = null,
    val lastDisconnectedAtMillis: Long? = null,
    val lastDisconnectReason: String? = null
)

object RemoteConnectionRuntimeState {
    private val current = AtomicReference(RemoteConnectionSnapshot())

    fun snapshot(): RemoteConnectionSnapshot = current.get()

    fun markDisabled(
        homeAssistantUrl: String? = null,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                status = RemoteConnectionStatus.Disabled,
                homeAssistantUrl = homeAssistantUrl,
                lastDisconnectedAtMillis = nowMillis,
                lastDisconnectReason = "disabled"
            )
        }
    }

    fun markConnecting(homeAssistantUrl: String, nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                status = RemoteConnectionStatus.Connecting,
                homeAssistantUrl = homeAssistantUrl,
                lastError = null,
                connectionAttemptCount = snapshot.connectionAttemptCount + 1,
                lastConnectionAttemptAtMillis = nowMillis
            )
        }
    }

    fun markConnected(homeAssistantUrl: String, nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                status = RemoteConnectionStatus.Connected,
                homeAssistantUrl = homeAssistantUrl,
                lastError = null,
                connectedAtMillis = nowMillis,
                lastMessageAtMillis = nowMillis,
                successfulConnectionCount = snapshot.successfulConnectionCount + 1
            )
        }
    }

    fun markMessage(nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                lastMessageAtMillis = nowMillis,
                messageCount = snapshot.messageCount + 1
            )
        }
    }

    fun markDisconnected(error: String? = null, nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                status = if (error == null) {
                    RemoteConnectionStatus.Disconnected
                } else {
                    RemoteConnectionStatus.Error
                },
                lastError = error,
                lastDisconnectedAtMillis = nowMillis,
                lastDisconnectReason = error
            )
        }
    }

    fun resetForTest() {
        current.set(RemoteConnectionSnapshot())
    }
}
