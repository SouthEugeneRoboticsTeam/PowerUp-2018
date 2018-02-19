package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class DriveToCube : Command() {
    private val table = NetworkTableInstance.getDefault().getTable("Vision")

    override fun execute(): Boolean {
        if (!table.getEntry("cube_found").getBoolean(false)) return true

        val degrees = table.getEntry("cube_offset_x").getDouble(0.0) * DEGREES_PER_PIXEL
        val turn = degrees * TURN_MODIFIER

        Drivetrain.tank(BASE_SPEED - turn, BASE_SPEED + turn)

        return false
    }

    private companion object {
        val BASE_SPEED = 0.25
        val TURN_MODIFIER = 0.01
    }
}
