package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sertain.command.Command

class NavigateToCube : Command() {
    private var cachedNTLoc = Pair(-1, -1)
    private var ntTable = NetworkTableInstance.getDefault()

    override fun execute(): Boolean {
        if (!ntTable.isValid) return true
        val didFindCube = ntTable.getEntry("cube_found").getBoolean(false)
        if (!didFindCube) return true // Maybe we should do something else if we can't find a cube, instead of aborting the command
        val x = ntTable.getEntry("cube_offset_x").getNumber(0).toInt()
        val y = ntTable.getEntry("cube_offset_y").getNumber(0).toInt()
        // Update cache if it's something new
        if (x != 0 && y != 0) cachedNTLoc = Pair(x, y)
        TODO("Add drivetrain manipulation")
    }
}
