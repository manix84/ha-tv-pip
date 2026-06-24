package com.hatvpip.receiver

import android.content.Context
import java.security.SecureRandom
import java.util.Base64

data class PairingSnapshot(
    val state: PairingStatus,
    val pairingRequired: Boolean,
    val pendingCode: String? = null,
    val pendingClientName: String? = null,
    val pendingReplacesExisting: Boolean = false,
    val pairedClientName: String? = null
)

enum class PairingStatus(val wireName: String) {
    Unpaired("required"),
    Pending("pending"),
    Paired("paired")
}

data class PairingStartRequest(
    val clientId: String,
    val clientName: String,
    val replaceExisting: Boolean = false
)

data class PairingConfirmRequest(
    val clientId: String,
    val clientName: String,
    val code: String
)

data class PairingResult(
    val token: String,
    val clientId: String,
    val clientName: String
)

object PairingState {
    private const val PREFS_NAME = "ha_tv_pip_pairing"
    private const val KEY_TOKEN = "token"
    private const val KEY_CLIENT_ID = "client_id"
    private const val KEY_CLIENT_NAME = "client_name"
    private const val PAIRING_CODE_TTL_MS = 5 * 60 * 1_000L
    private val random = SecureRandom()

    @Volatile
    private var pending: PendingPairing? = null

    fun snapshot(context: Context, nowMillis: Long = System.currentTimeMillis()): PairingSnapshot {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val pairedClientName = prefs.getString(KEY_CLIENT_NAME, null)
        val hasStoredPairing = pairedClientName != null && prefs.getString(KEY_TOKEN, null) != null
        val activePending = pending?.takeIf { it.expiresAtMillis > nowMillis }
        if (activePending != null) {
            return PairingSnapshot(
                state = PairingStatus.Pending,
                pairingRequired = !hasStoredPairing,
                pendingCode = activePending.code,
                pendingClientName = activePending.clientName,
                pendingReplacesExisting = activePending.replaceExisting,
                pairedClientName = pairedClientName
            )
        }

        pending = null
        if (hasStoredPairing) {
            return PairingSnapshot(
                state = PairingStatus.Paired,
                pairingRequired = false,
                pairedClientName = pairedClientName
            )
        }

        return PairingSnapshot(
            state = PairingStatus.Unpaired,
            pairingRequired = true
        )
    }

    fun startPairing(
        context: Context,
        request: PairingStartRequest,
        nowMillis: Long = System.currentTimeMillis()
    ): Result<PairingSnapshot> {
        val current = snapshot(context, nowMillis)
        if (current.state == PairingStatus.Paired && !request.replaceExisting) {
            return Result.failure(IllegalStateException("already_paired"))
        }

        pending = PendingPairing(
            clientId = request.clientId,
            clientName = request.clientName,
            replaceExisting = request.replaceExisting,
            code = generateCode(),
            expiresAtMillis = nowMillis + PAIRING_CODE_TTL_MS
        )
        return Result.success(snapshot(context, nowMillis))
    }

    fun confirmPairing(
        context: Context,
        request: PairingConfirmRequest,
        nowMillis: Long = System.currentTimeMillis()
    ): Result<PairingResult> {
        val activePending = pending?.takeIf { it.expiresAtMillis > nowMillis }
            ?: return Result.failure(IllegalArgumentException("pairing_not_started"))

        if (activePending.clientId != request.clientId) {
            return Result.failure(IllegalArgumentException("invalid_client"))
        }

        if (activePending.code != request.code.trim()) {
            return Result.failure(IllegalArgumentException("invalid_code"))
        }

        val clientName = request.clientName.ifBlank { activePending.clientName }
        val token = generateToken()
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_CLIENT_ID, request.clientId)
            .putString(KEY_CLIENT_NAME, clientName)
            .apply()
        pending = null

        return Result.success(
            PairingResult(
                token = token,
                clientId = request.clientId,
                clientName = clientName
            )
        )
    }

    fun reset(context: Context) {
        pending = null
        clearStoredPairing(context)
    }

    fun isAuthorized(context: Context, token: String?): Boolean {
        val storedToken = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
        return storedToken != null && token != null && constantTimeEquals(storedToken, token)
    }

    fun pairedToken(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)

    private fun clearStoredPairing(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TOKEN)
            .remove(KEY_CLIENT_ID)
            .remove(KEY_CLIENT_NAME)
            .apply()
    }

    private fun generateCode(): String =
        (100_000 + random.nextInt(900_000)).toString()

    private fun generateToken(): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun constantTimeEquals(left: String, right: String): Boolean {
        val leftBytes = left.encodeToByteArray()
        val rightBytes = right.encodeToByteArray()
        if (leftBytes.size != rightBytes.size) return false

        var diff = 0
        leftBytes.indices.forEach { index ->
            diff = diff or (leftBytes[index].toInt() xor rightBytes[index].toInt())
        }
        return diff == 0
    }

    private data class PendingPairing(
        val clientId: String,
        val clientName: String,
        val replaceExisting: Boolean,
        val code: String,
        val expiresAtMillis: Long
    )
}
