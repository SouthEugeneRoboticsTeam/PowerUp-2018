package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand

abstract class AngleDriver(p: Double, i: Double = 0.0, d: Double = 0.0) : PidCommand(p, i, d) {
    init {
        requires(Drivetrain)
    }

    override fun returnPidInput() = Drivetrain.ahrs.yaw.toDouble()

    override fun onDestroy() = Drivetrain.stop()
}
