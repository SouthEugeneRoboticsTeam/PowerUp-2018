package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DigitalOutput
import edu.wpi.first.wpilibj.Timer.getMatchTime
import org.sert2521.powerup.intake.Intake
import org.sertain.command.Command
import org.sertain.command.Subsystem

object Lights : Subsystem() {
    private val redChannel = DigitalOutput(RED_LED_PORT)
    private val blueChannel = DigitalOutput(BLUE_LED_PORT)

    override val defaultCommand = Light()

    /**
     * Change light settings depending on current robot status:
     *
     * 27 > time > 30  ->  (true, true)
     * has cube        ->  (false, true)
     * sees cube       ->  (true, false)
     * idle            ->  (false, false)
     */
    fun setLights() {
        if (getMatchTime() < 30 && getMatchTime() > 27) {
            redChannel.set(true)
            blueChannel.set(true)
        } else if (Intake.hasCube) {
            redChannel.set(false)
            blueChannel.set(true)
        } else if (Vision.found == true) {
            redChannel.set(true)
            blueChannel.set(false)
        } else {
            redChannel.set(false)
            blueChannel.set(false)
        }
    }
}

class Light : Command() {
    init {
        requires(Lights)
    }

    override fun execute(): Boolean {
        Lights.setLights()
        return false
    }
}
