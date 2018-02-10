package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DigitalInput
import org.sertain.hardware.Talon
import org.sertain.command.Command

class SadFlimsyEncoder(channel: Int, talon: Talon) {
    private val digitalInput = DigitalInput(channel)
    private var revolutions = 0

    init {
        object : Command() {
            var lastTime = digitalInput.get()
            var now = false
            override fun execute(): Boolean {
                now = digitalInput.get()
                if (!lastTime && now) when {
                    talon.get() > 0 -> revolutions++
                    talon.get() < 0 -> revolutions--
                }
                lastTime = now
                return false
            }
        }.start()
    }

    fun get() = revolutions
}
