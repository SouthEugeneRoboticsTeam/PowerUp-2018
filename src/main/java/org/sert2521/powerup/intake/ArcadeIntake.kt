package org.sert2521.powerup.intake

import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class ArcadeIntake : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.runJoystick()
        return false
    }

    override fun onDestroy() {
        Intake.stop()
    }
}
