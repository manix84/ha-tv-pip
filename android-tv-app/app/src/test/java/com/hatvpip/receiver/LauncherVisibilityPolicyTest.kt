package com.hatvpip.receiver

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LauncherVisibilityPolicyTest {
    @Test
    fun unpairedReceiverCannotHideLauncher() {
        assertFalse(LauncherVisibilityPolicy.canHideLauncher(PairingStatus.Unpaired))
        assertTrue(
            LauncherVisibilityPolicy.visibilityForRequest(
                requestedVisible = false,
                pairingStatus = PairingStatus.Unpaired
            )
        )
    }

    @Test
    fun pendingReceiverCannotHideLauncher() {
        assertFalse(LauncherVisibilityPolicy.canHideLauncher(PairingStatus.Pending))
        assertTrue(
            LauncherVisibilityPolicy.visibilityForRequest(
                requestedVisible = false,
                pairingStatus = PairingStatus.Pending
            )
        )
    }

    @Test
    fun pairedReceiverCanHideLauncher() {
        assertTrue(LauncherVisibilityPolicy.canHideLauncher(PairingStatus.Paired))
        assertFalse(
            LauncherVisibilityPolicy.visibilityForRequest(
                requestedVisible = false,
                pairingStatus = PairingStatus.Paired
            )
        )
    }

    @Test
    fun hiddenLauncherIsRestoredWhenPairingIsMissing() {
        assertTrue(
            LauncherVisibilityPolicy.shouldRestoreLauncher(
                pairingStatus = PairingStatus.Unpaired,
                launcherVisible = false
            )
        )
        assertFalse(
            LauncherVisibilityPolicy.shouldRestoreLauncher(
                pairingStatus = PairingStatus.Paired,
                launcherVisible = false
            )
        )
    }
}
