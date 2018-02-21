package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command

/**
 * Drives at a specified fixed speed while cancelling all other [Drivetrain] commands.
 */
class FixedDrive(private val leftSpeed: Double, private val rightSpeed: Double) : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.drive(leftSpeed, rightSpeed)
        return true
    }
}
