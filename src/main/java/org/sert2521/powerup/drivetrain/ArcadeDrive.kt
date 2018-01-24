package org.sert2521.powerup.drivetrain

import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class ArcadeDrive : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.arcade()
        return false
    }

    override fun onDestroy() {
        Drivetrain.stop()
    }
}
