package org.sert2521.powerup.util

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.Date

object TimeSync : Thread() {
    // Time (in secs) to wait between syncs
    private const val WAIT_PERIOD: Long = 5000

    // UDP socket
    private val socket = DatagramSocket().apply { broadcast = true } // Broadcast is true by default, but it helps to specify

    override fun run() {
        // Init socket
        socket.connect(InetAddress.getByName(BROADCAST_IP), JETSON_PORT)
        while (true) {
            val epoch = Date().toInstant().toEpochMilli()
            val msg = "${epochSecs(epoch)}-${epochMillis(epoch)}".toByteArray()
            val packet = DatagramPacket(msg, msg.size)
            socket.send(packet)
            sleep(WAIT_PERIOD)
        }
    }

    // Extract seconds from an epoch number
    private fun epochSecs(epoch: Long): Long {
        return epoch.toString().let {
            it.substring(0, it.length - 3).toLong()
        }
    }

    // Extract milliseconds from the epoch number
    private fun epochMillis(epoch: Long): Long {
        return epoch.toString().let {
            it.substring(it.length - 3).toLong()
        }
    }
}
