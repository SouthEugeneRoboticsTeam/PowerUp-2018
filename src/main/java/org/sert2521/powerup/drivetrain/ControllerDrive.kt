package org.sert2521.powerup.drivetrain

import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class ControllerDrive : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.controllerDrive()
        return false
    }

    override fun onDestroy() {
        Drivetrain.stop()
    }
}
