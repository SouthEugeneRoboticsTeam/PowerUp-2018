package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.DEGREES_PER_PIXEL
import org.sert2521.powerup.util.VisionData

class DriveToCube : AngleDriver(0.01, 0.0, 0.022) {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() = updateSetpoint(0.0)

    override fun execute(output: Double): Boolean {
        println(VisionData.xOffset)
        Drivetrain.drive(BASE_SPEED + output, BASE_SPEED - output)
        updateSetpoint(VisionData.xOffset * DEGREES_PER_PIXEL)
        return Intake.hasCube && Elevator.atBottom
    }

    private fun updateSetpoint(offset: Double) {
        setpoint = Drivetrain.ahrs.angle + offset
    }

    private companion object {
        const val BASE_SPEED = 0.3
    }
}
