package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sertain.command.Command
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class DriveToCube : Command() {
    private val table = NetworkTableInstance.getDefault().getTable("Vision")
    private var previousDegreeValue = 0.0

    override fun execute(): Boolean {
        if (!table.getEntry("cube_found").getBoolean(false)) return true

        val degrees = table.getEntry("cube_offset_x").getDouble(0.0) * DEGREES_PER_PIXEL

        if (degrees != previousDegreeValue) {
            DriveToAngle(degrees, BASE_SPEED).start()
            previousDegreeValue = degrees
        }
        
        return false
    }

    private companion object {
        val BASE_SPEED = 0.25
    }
}
