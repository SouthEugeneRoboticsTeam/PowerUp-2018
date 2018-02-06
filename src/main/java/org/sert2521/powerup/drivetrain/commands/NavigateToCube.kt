package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command
import org.sert2521.powerup.util.DEGREES_PER_PIXEL

class NavigateToCube : Command() {
    private var table = NetworkTableInstance.getDefault().getTable("vision")

    override fun execute(): Boolean {
        val foundCube = table.getEntry("cube_found").getBoolean(false)

        if (!foundCube) return true
        
        val x = table.getEntry("cube_offset_x").getNumber(0).toInt()

        val degrees = x * DEGREES_PER_PIXEL
        val turn = degrees * TURN_MODIFIER
        val left = BASE_SPEED + turn
        val right = BASE_SPEED - turn

        Drivetrain.tank(left, right)

        return false
    }

    companion object {
        val BASE_SPEED = 0.25
        val TURN_MODIFIER = 0.01
    }
}
