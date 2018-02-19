package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class DriveToCube : DriveToAngle(0.0, BASE_SPEED) {
    override fun execute(): Boolean {
        if (!table.getEntry("cube_found").getBoolean(false)) return true
        adjustedSetpoint = table.getEntry("cube_offset_x").getDouble(0.0) * DEGREES_PER_PIXEL
        return false
    }

    private companion object {
        const val BASE_SPEED = 0.25
        val table: NetworkTable = NetworkTableInstance.getDefault().getTable("Vision")
    }
}
