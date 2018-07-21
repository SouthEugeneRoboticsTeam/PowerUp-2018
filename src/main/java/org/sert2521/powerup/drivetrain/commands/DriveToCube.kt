package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.DEGREES_PER_PIXEL
import org.sert2521.powerup.util.Vision

class DriveToCube : AngleDriver(0.01, 0.0, 0.022) {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() = updateSetpoint(0.0)

    override fun execute(output: Double): Boolean {
        println("Found Cube: ${Vision.found}, " +
                "X Offset: ${Vision.xOffset}, " +
                "Y Offset: ${Vision.yOffset}")

        if (Vision.found == true) {
            Drivetrain.drive(BASE_SPEED + output, BASE_SPEED - output)
            updateSetpoint(Vision.xOffset?.toDouble() ?: 0.0 * DEGREES_PER_PIXEL)
            return Intake.hasCube && Elevator.atBottom
        }

        return true
    }

    private fun updateSetpoint(offset: Double) {
        setpoint = Drivetrain.ahrs.angle + offset
    }

    private companion object {
        const val BASE_SPEED = 0.3
    }
}
