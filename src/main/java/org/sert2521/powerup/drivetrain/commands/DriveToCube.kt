package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class DriveToCube : AngleDriver(0.01) {
    override fun onCreate() = updateSetpoint(0.0)

    override fun execute(output: Double): Boolean {
        Drivetrain.drive(BASE_SPEED + output, BASE_SPEED - output)
        updateSetpoint(table.getEntry("cube_offset_x").getDouble(0.0) * DEGREES_PER_PIXEL)
        return Intake.hasCube && Elevator.atBottom
    }

    private fun updateSetpoint(offset: Double) {
        setpoint = Drivetrain.ahrs.yaw + offset
    }

    private companion object {
        const val BASE_SPEED = 0.3
        val table: NetworkTable = NetworkTableInstance.getDefault().getTable("Vision")
    }
}
