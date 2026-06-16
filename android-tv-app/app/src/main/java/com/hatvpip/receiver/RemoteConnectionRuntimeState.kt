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
    val lastMessageAtMillis: Long? = null
)

object RemoteConnectionRuntimeState {
    private val current = AtomicReference(RemoteConnectionSnapshot())

    fun snapshot(): RemoteConnectionSnapshot = current.get()

    fun markDisabled(homeAssistantUrl: String? = null) {
        current.set(
            RemoteConnectionSnapshot(
                status = RemoteConnectionStatus.Disabled,
                homeAssistantUrl = homeAssistantUrl
            )
        )
    }

    fun markConnecting(homeAssistantUrl: String) {
        current.set(
            RemoteConnectionSnapshot(
                status = RemoteConnectionStatus.Connecting,
                homeAssistantUrl = homeAssistantUrl
            )
        )
    }

    fun markConnected(homeAssistantUrl: String, nowMillis: Long = System.currentTimeMillis()) {
        current.set(
            RemoteConnectionSnapshot(
                status = RemoteConnectionStatus.Connected,
                homeAssistantUrl = homeAssistantUrl,
                connectedAtMillis = nowMillis,
                lastMessageAtMillis = nowMillis
            )
        )
    }

    fun markMessage(nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(lastMessageAtMillis = nowMillis)
        }
    }

    fun markDisconnected(error: String? = null) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                status = if (error == null) {
                    RemoteConnectionStatus.Disconnected
                } else {
                    RemoteConnectionStatus.Error
                },
                lastError = error
            )
        }
    }
}
