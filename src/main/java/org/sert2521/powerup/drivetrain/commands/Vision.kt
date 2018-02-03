package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.networktables.NetworkTableInstance
import org.sertain.command.Command

class NavigateToCube : Command() {
    private var cached_nt_loc = Pair(-1, -1)
    private var nt_table = NetworkTableInstance.getDefault()
    override fun execute(): Boolean {
        if (!nt_table.isValid) {
            return true
        }
        val did_find_cube = nt_table.getEntry("cube_found").getBoolean(false)
        if (!did_find_cube) {
            return true // Maybe we should do something else if we can't find a cube, instead of aborting the command
        }
        val x = nt_table.getEntry("cube_offset_x").getNumber(0).toInt()
        val y = nt_table.getEntry("cube_offset_y").getNumber(0).toInt()
        // Update cache if it's something new
        if (x != 0 && y != 0) {
            cached_nt_loc = Pair(x, y)
        }
        TODO("Add drivetrain manipulation")
    }
}