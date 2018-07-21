package org.sert2521.powerup.util

import com.google.gson.Gson
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.net.DatagramPacket
import java.net.DatagramSocket

object UDPServer : Thread() {
    private const val PACKET_SIZE = 128

    private val socket = DatagramSocket(UDP_PORT)
    private val gson = Gson()

    override fun run() {
        while (true) {
            val buf = ByteArray(PACKET_SIZE)
            val packet = DatagramPacket(buf, buf.size)

            socket.receive(packet)
            val msg = String(packet.data).trim { it <= ' ' }

            gson.fromJson(msg, VisionData::class.java).also {
                Vision.apply {
                    if (it.alive == null) {
                        found = it.found
                        xOffset = it.xOffset
                        yOffset = it.yOffset

                        SmartDashboard.putBoolean("vision_found", found ?: false)
                        SmartDashboard.putNumber("vision_offset", xOffset?.toDouble() ?: 0.0)
                    } else {
                        alive = it.alive
                    }

                    time = it.time
                }
            }
        }
    }
}
