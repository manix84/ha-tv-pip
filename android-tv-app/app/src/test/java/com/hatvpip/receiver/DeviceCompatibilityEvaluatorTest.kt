package com.hatvpip.receiver

import android.view.Gravity
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceCompatibilityEvaluatorTest {
    @Test
    fun nativePipIsRecommendedWhenFeatureIsExposed() {
        val compatibility = DeviceCompatibilityEvaluator.evaluate(
            sdkInt = 34,
            release = "14",
            hasNativePipFeature = true,
            overlayGranted = false
        )

        assertEquals(CompatibilityState.Supported, compatibility.nativePictureInPicture)
        assertEquals(ReceiverDisplayMode.NativePictureInPicture, compatibility.recommendedMode)
    }

    @Test
    fun fullScreenFallbackIsRecommendedWhenNativePipIsNotExposed() {
        val compatibility = DeviceCompatibilityEvaluator.evaluate(
            sdkInt = 34,
            release = "14",
            hasNativePipFeature = false,
            overlayGranted = false
        )

        assertEquals(CompatibilityState.NotSupported, compatibility.nativePictureInPicture)
        assertEquals(ReceiverDisplayMode.FullScreenFallback, compatibility.recommendedMode)
    }

    @Test
    fun overlayFallbackIsRecommendedWhenPermissionIsGranted() {
        val compatibility = DeviceCompatibilityEvaluator.evaluate(
            sdkInt = 34,
            release = "14",
            hasNativePipFeature = false,
            overlayGranted = true
        )

        assertEquals(CompatibilityState.Granted, compatibility.overlayPermission)
        assertEquals(ReceiverDisplayMode.OverlayFallback, compatibility.recommendedMode)
    }

    @Test
    fun nativePipUsesOverlayForEveryNonDefaultCornerWhenPermissionIsGranted() {
        val compatibility = DeviceCompatibilityEvaluator.evaluate(
            sdkInt = 30,
            release = "11",
            hasNativePipFeature = true,
            overlayGranted = true
        )

        val expectedModes = mapOf(
            NotificationPosition.TopRight to ReceiverDisplayMode.NativePictureInPicture,
            NotificationPosition.TopLeft to ReceiverDisplayMode.OverlayFallback,
            NotificationPosition.BottomRight to ReceiverDisplayMode.OverlayFallback,
            NotificationPosition.BottomLeft to ReceiverDisplayMode.OverlayFallback
        )

        expectedModes.forEach { (position, expectedMode) ->
            assertEquals(position.wireName, expectedMode, compatibility.displayModeFor(position))
        }
    }

    @Test
    fun nativePipRemainsTheFallbackWhenOverlayPermissionIsMissing() {
        val compatibility = DeviceCompatibilityEvaluator.evaluate(
            sdkInt = 30,
            release = "11",
            hasNativePipFeature = true,
            overlayGranted = false
        )

        NotificationPosition.entries.forEach { position ->
            assertEquals(
                position.wireName,
                ReceiverDisplayMode.NativePictureInPicture,
                compatibility.displayModeFor(position)
            )
        }
    }

    @Test
    fun overlayGravityMapsEveryPositionToItsRequestedCorner() {
        val expectedGravity = mapOf(
            NotificationPosition.TopRight to (Gravity.TOP or Gravity.END),
            NotificationPosition.TopLeft to (Gravity.TOP or Gravity.START),
            NotificationPosition.BottomRight to (Gravity.BOTTOM or Gravity.END),
            NotificationPosition.BottomLeft to (Gravity.BOTTOM or Gravity.START)
        )

        expectedGravity.forEach { (position, gravity) ->
            assertEquals(position.wireName, gravity, overlayGravityFor(position))
        }
    }
}
