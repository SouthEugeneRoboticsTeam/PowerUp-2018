package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class DriveToCube : DriveToAngle(0.0, BASE_SPEED, p = 0.01) {
    override fun execute(output: Double): Boolean {
        super.execute(output)

        if (!table.getEntry("cube_found").getBoolean(false)) return true
        setpoint = Drivetrain.ahrs.angle + table.getEntry("cube_offset_x").getDouble(0.0) * DEGREES_PER_PIXEL

        return Intake.hasCube
    }

    private companion object {
        const val BASE_SPEED = 0.3
        val table: NetworkTable = NetworkTableInstance.getDefault().getTable("Vision")
    }
}
