package org.sert2521.powerup.drivetrain

import org.sertain.command.Command

class FixedDrive(private val speed: Double) : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.tank(speed, speed)
        return true
    }
}
