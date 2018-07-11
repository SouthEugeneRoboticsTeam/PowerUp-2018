package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DigitalOutput
import edu.wpi.first.wpilibj.Timer
import org.sert2521.powerup.intake.Intake
import org.sertain.command.Command
import org.sertain.command.Subsystem

object Lights : Subsystem() {
    private val redLEDChannel = DigitalOutput(RED_LED_PORT)
    private val blueLEDChannel = DigitalOutput(BLUE_LED_PORT)
    private val timer = Timer()
    private val time = timer.get()

    override val defaultCommand = Light()

    fun setLEDs() {
        timer.start()
        when {
            time > 120000 && time < 1230000 -> {
                redLEDChannel.set(false)
                blueLEDChannel.set(false)
            }
            VisionData.foundCube -> {
                redLEDChannel.set(true)
                blueLEDChannel.set(false)
            }
            Intake.hasCube && VisionData.foundCube -> {
                redLEDChannel.set(false)
                blueLEDChannel.set(true)
            }
            else -> {
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
