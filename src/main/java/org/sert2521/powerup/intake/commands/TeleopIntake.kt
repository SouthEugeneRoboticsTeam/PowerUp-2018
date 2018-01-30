package org.sert2521.powerup.intake.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class TeleopIntake : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        when (controlMode) {
            Control.Arcade, Control.Tank -> Intake.set(when {
                rightJoystick.trigger -> 0.5
                rightJoystick.top -> -0.5
                else -> 0.0
            })
            Control.Controller -> {
                val leftSpeed = controller.getTriggerAxis(GenericHID.Hand.kLeft)
                val rightSpeed = controller.getTriggerAxis(GenericHID.Hand.kRight)

                // Support variable intake speeds though self-cancellation
                Intake.set(leftSpeed - rightSpeed)
            }
        }

        return false
    }

    override fun onDestroy() = Intake.stop()
}
