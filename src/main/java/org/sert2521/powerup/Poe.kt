package org.sert2521.powerup

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.powerup.autonomous.Auto
import org.sert2521.powerup.climber.Climber
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Modes
import org.sert2521.powerup.util.lightpad
import org.sertain.Robot

class Poe : Robot() {
    override fun onCreate() {
        Drivetrain
        Intake
        Elevator
        Climber
        Auto
        Modes

        CameraServer.getInstance().startAutomaticCapture()
    }

    override fun executeAuto() {
        val time = DriverStation.getInstance().matchTime
        val percent = Math.floor(time / 15 * 100).toInt()

        updateLights(percent)
    }

    override fun executeTeleop() {
        val time = DriverStation.getInstance().matchTime
        val percent = Math.floor(time / 135 * 100).toInt()

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
