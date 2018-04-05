package org.sert2521.powerup.lights

import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.powerup.util.lightpad
import org.sertain.command.Subsystem

object Lights : Subsystem() {
    private const val AUTO_TIME = 15
    private const val TELEOP_TIME = 135

    override fun executeAuto() {
        val time = DriverStation.getInstance().matchTime
        val percent = Math.floor(time / AUTO_TIME * 100).toInt()

        updateLights(percent)
    }

    override fun executeTeleop() {
        val time = DriverStation.getInstance().matchTime
        val percent = Math.floor(time / TELEOP_TIME * 100).toInt()

        updateLights(percent)
    }

    private fun updateLights(time: Int) {
        val binary = Integer.toBinaryString(time)
                .padStart(7, '0')
                .map { it == '1' }
                .take(7)

        binary.forEachIndexed { index, value -> lightpad.setOutput(index + 1, value) }

        lightpad.setOutput(8, DriverStation.getInstance().isEnabled)
        lightpad.setOutput(9, DriverStation.getInstance().isAutonomous)
        lightpad.setOutput(10, DriverStation.getInstance().alliance === DriverStation.Alliance.Blue)
    }
}