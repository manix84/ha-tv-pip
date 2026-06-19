package com.hatvpip.receiver

import java.util.concurrent.atomic.AtomicReference

data class ReceiverServiceSnapshot(
    val running: Boolean = false,
    val foreground: Boolean = false,
    val startCount: Long = 0,
    val lastStartReason: String? = null,
    val lastStartedAtMillis: Long? = null,
    val lastDestroyedAtMillis: Long? = null,
    val lastBootReceiverAction: String? = null,
    val lastBootReceiverAtMillis: Long? = null
)

object ReceiverServiceRuntimeState {
    private val current = AtomicReference(ReceiverServiceSnapshot())

    fun snapshot(): ReceiverServiceSnapshot = current.get()

    fun markBootReceiverAction(action: String, nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                lastBootReceiverAction = action,
                lastBootReceiverAtMillis = nowMillis
            )
        }
    }

    fun markForegroundStarted() {
        current.updateAndGet { snapshot ->
            snapshot.copy(foreground = true)
        }
    }

    fun markServiceStarted(
        reason: String,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                running = true,
                startCount = snapshot.startCount + 1,
                lastStartReason = reason,
                lastStartedAtMillis = nowMillis
            )
        }
    }

    fun markServiceDestroyed(nowMillis: Long = System.currentTimeMillis()) {
        current.updateAndGet { snapshot ->
            snapshot.copy(
                running = false,
                foreground = false,
                lastDestroyedAtMillis = nowMillis
            )
        }
    }

    fun resetForTest() {
        current.set(ReceiverServiceSnapshot())
    }
}
