package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Test

class ReceiverNameSettingsTest {
    @Test
    fun sanitizeNameTrimsAndCollapsesWhitespace() {
        assertEquals(
            "Living Room Chromecast",
            ReceiverNameSettings.sanitizeNameForTest("  Living   Room\nChromecast  ")
        )
    }

    @Test
    fun sanitizeNameLimitsLength() {
        val longName = "x".repeat(80)

        assertEquals(64, ReceiverNameSettings.sanitizeNameForTest(longName).length)
    }
}
