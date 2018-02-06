package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command

const val VISION_CAMERA_FOV = 60 // Microsoft LifeCam HD 3000 FOV
const val CAMERA_IMAGE_PIXEL_COUNT = 680
const val DEGREES_PER_PIXEL = VISION_CAMERA_FOV / CAMERA_IMAGE_PIXEL_COUNT
const val BASE_SPEED = 0.25
const val TURN_MODIFIER = 0.01

class NavigateToCube : Command() {
    private var cachedOffset = -1

    private var ntTable = NetworkTableInstance.getDefault()

    override fun execute(): Boolean {
        if (!ntTable.isValid) return true

        val didFindCube =
                ntTable.getEntry("cube_found").getBoolean(false)

        if (!didFindCube) return true
        
        val x =
                ntTable.getEntry("cube_offset_x").getNumber(0).toInt()

        // Update cache if it's something new

        if (x != 0) cachedOffset = x

        if (cachedOffset == -1) return true

        val degrees =
                cachedOffset * DEGREES_PER_PIXEL
        val turn =
                degrees * TURN_MODIFIER
        val left =
                BASE_SPEED + turn
        val right =
                BASE_SPEED - turn

        Drivetrain.tank(left, right)
        return false
    }
}
