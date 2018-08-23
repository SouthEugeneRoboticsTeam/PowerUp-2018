package org.sert2521.powerup.util

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.Date

object TimeSync : Thread() {
    // Time (in secs) to wait between syncs
    private const val WAIT_PERIOD: Long = 5
    // UDP port
    private const val PORT: Int = 2521
    // Change this to the Jetson's IP before deploying
    private const val JETSON_IP = "127.0.0.1"

    // UDP socket
    private val socket = DatagramSocket()

    override fun run() {
        // Init socket
        socket.connect(InetAddress.getByName(JETSON_IP), PORT)
        while (true) {
            val epoch = Date().toInstant().toEpochMilli()
            val msg = "${epoch_secs(epoch)}-${epoch_millis(epoch)}".toByteArray()
            val packet = DatagramPacket(msg, msg.size)
            socket.send(packet)
            sleep(WAIT_PERIOD * 1000)
        }
    }

    // Extract seconds from an epoch number
    private fun epoch_secs(epoch: Long): Long {
        val str = epoch.toString()
        return str.substring(0, str.length - 3).toLong()
    }

    // Extract milliseconds from the epoch number
    private fun epoch_millis(epoch: Long): Long {
        val str = epoch.toString()
        return str.substring(str.length - 3).toLong()
    }
}
