package com.hatvpip.receiver

import java.net.Inet4Address
import java.net.NetworkInterface

data class ControlEndpointInfo(
    val port: Int = LocalControlServer.DEFAULT_PORT,
    val localIpAddress: String? = findLocalIpv4Address()
) {
    val displayAddress: String
        get() = localIpAddress?.let { "$it:$port" } ?: "Port $port"
}

private fun findLocalIpv4Address(): String? =
    runCatching {
        NetworkInterface.getNetworkInterfaces()
        .asSequence()
        .filter { it.isUp && !it.isLoopback }
        .flatMap { it.inetAddresses.asSequence() }
        .filterIsInstance<Inet4Address>()
        .firstOrNull { !it.isLoopbackAddress }
        ?.hostAddress
    }.getOrNull()
