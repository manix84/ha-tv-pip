package com.hatvpip.receiver

import java.util.concurrent.atomic.AtomicReference

data class ControlRequestSnapshot(
    val method: String,
    val path: String,
    val status: Int,
    val timestampMillis: Long
)

data class ControlServerSnapshot(
    val running: Boolean = false,
    val port: Int = LocalControlServer.DEFAULT_PORT,
    val startedAtMillis: Long? = null,
    val requestCount: Long = 0,
    val lastRequest: ControlRequestSnapshot? = null
) {
    fun uptimeSeconds(nowMillis: Long): Long =
        startedAtMillis?.let { ((nowMillis - it) / 1_000L).coerceAtLeast(0L) } ?: 0L
}

object ControlRuntimeState {
    private val current = AtomicReference(ControlServerSnapshot())

    fun snapshot(): ControlServerSnapshot = current.get()

    fun markStarted(port: Int, nowMillis: Long = System.currentTimeMillis()) {
        current.set(
            ControlServerSnapshot(
                running = true,
                port = port,
                startedAtMillis = nowMillis
            )
        )
    }

    fun markStopped() {
        current.updateAndGet { snapshot ->
            snapshot.copy(running = false)
        }
    }

    fun recordRequest(
        method: String,
        path: String,
        status: Int,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                requestCount = snapshot.requestCount + 1,
                lastRequest = ControlRequestSnapshot(
                    method = method,
                    path = path,
                    status = status,
                    timestampMillis = nowMillis
                )
            )
        }
    }
}
