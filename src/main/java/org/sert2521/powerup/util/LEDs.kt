/*
package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DigitalOutput
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Subsystem
import org.sert2521.powerup.intake.Intake
import kotlin.concurrent.timer

object Lights : Subsystem() {
    private val redLEDChannel = DigitalOutput(RED_LED_PORT)
    private val blueLEDChannel = DigitalOutput(BLUE_LED_PORT)
    private val timer = Timer()


    fun setLEDs() {
        if (Intake.hasCube) {
            redLEDChannel.set(false)
            blueLEDChannel.set(true)
        } else if (VisionData.foundCube) {
            redLEDChannel.set(true)
            blueLEDChannel.set(false)
        }
    }
}*/
