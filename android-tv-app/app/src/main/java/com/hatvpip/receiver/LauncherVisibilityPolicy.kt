package com.hatvpip.receiver

object LauncherVisibilityPolicy {
    fun canHideLauncher(pairingStatus: PairingStatus): Boolean =
        pairingStatus == PairingStatus.Paired

    fun visibilityForRequest(requestedVisible: Boolean, pairingStatus: PairingStatus): Boolean =
        requestedVisible || !canHideLauncher(pairingStatus)

    fun shouldRestoreLauncher(pairingStatus: PairingStatus, launcherVisible: Boolean): Boolean =
        !launcherVisible && !canHideLauncher(pairingStatus)
}
