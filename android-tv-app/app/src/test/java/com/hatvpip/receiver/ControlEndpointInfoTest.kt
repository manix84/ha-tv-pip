package com.hatvpip.receiver

import org.junit.Assert.assertEquals
import org.junit.Test

class ControlEndpointInfoTest {
    @Test
    fun displayAddressUsesIpAndPortWhenIpIsKnown() {
        val info = ControlEndpointInfo(
            port = 8765,
            localIpAddress = "10.0.0.236"
        )

        assertEquals("10.0.0.236:8765", info.displayAddress)
    }

    @Test
    fun displayAddressFallsBackToPortWhenIpIsUnknown() {
        val info = ControlEndpointInfo(
            port = 8765,
            localIpAddress = null
        )

        assertEquals("Port 8765", info.displayAddress)
    }
}
