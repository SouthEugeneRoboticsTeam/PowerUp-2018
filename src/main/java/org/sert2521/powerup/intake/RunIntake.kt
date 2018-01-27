package org.sert2521.powerup.intake

import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class RunIntake : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.run()
        return false
    }

    override fun onDestroy() {
        Intake.stop()
    }
}
