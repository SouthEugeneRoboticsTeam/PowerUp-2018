package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command

/**
 * Drives at a specified fixed speed while cancelling all other [Drivetrain] commands.
 */
class FixedDrive(left: Double, right: Double) : Command() {
    private val leftSpeed: Double = left
    private val rightSpeed: Double = right

    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.drive(leftSpeed, rightSpeed)
        return true
    }
}
