package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DigitalOutput
import edu.wpi.first.wpilibj.Timer.getMatchTime
import org.sert2521.powerup.intake.Intake
import org.sertain.command.Command
import org.sertain.command.Subsystem

object Lights : Subsystem() {
    private val redLEDChannel = DigitalOutput(RED_LED_PORT)
    private val blueLEDChannel = DigitalOutput(BLUE_LED_PORT)

    override val defaultCommand = Light()

    fun setLEDs() {
        when {
            getMatchTime() > 120 && getMatchTime() < 123 -> {
                redLEDChannel.set(false)
                blueLEDChannel.set(false)
            }
            else -> if (VisionData.foundCube) {
                if (Intake.hasCube) {
                    redLEDChannel.set(false)
                    blueLEDChannel.set(true)
                } else {
                    redLEDChannel.set(true)
                    blueLEDChannel.set(false)
                }
            } else {
                redLEDChannel.set(true)
                blueLEDChannel.set(true)
            }
        }
    }
}

class Light : Command() {
    init {
        requires(Lights)
    }

    override fun execute(): Boolean {
        Lights.setLEDs()
        return false
    }
}
