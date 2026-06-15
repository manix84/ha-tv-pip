package com.hatvpip.receiver

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
}
