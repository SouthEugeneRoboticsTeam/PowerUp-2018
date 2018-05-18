package org.sert2521.powerup.util

import com.google.gson.Gson
import java.net.DatagramPacket
import java.net.DatagramSocket

object UDPServer : Thread() {
    private val PACKET_SIZE = 256

    private val socket = DatagramSocket(UDP_PORT)
    private val gson = Gson()

    override fun run() {
        val buf = ByteArray(PACKET_SIZE)
        val packet = DatagramPacket(buf, buf.size)

        socket.receive(packet)
        val msg = String(packet.data).trim { it <= ' ' }

        if (!msg.contains("alive")) {
            gson.fromJson(msg, VisionData.javaClass).also {
                VisionData.apply {
                    foundCube = it.foundCube
                    time = it.time
                    xOffset = it.xOffset
                    yOffset = it.yOffset
                }
            }

            println("Message from ${packet.address.hostAddress}: $msg")
        } else {
            println("Heartbeat from ${packet.address.hostAddress}")
        }
    }
}
