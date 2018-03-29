package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand

abstract class AngleDriver(p: Double, i: Double = 0.0, d: Double = 0.0) : PidCommand(p, i, d) {
    override fun returnPidInput() = Drivetrain.ahrs.angle

    override fun onDestroy() = Drivetrain.stop()
}
