package org.sert2521.powerup.climber.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command

/**
 * This command allows for teleoperated control of the robot's intake.
 */
class TeleopClimber : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        when (controlMode) {
            Control.Arcade, Control.Tank -> Intake.set(when {
                // TODO tune defaults
                rightJoystick.trigger -> intakeSpeedScalar * 1.0
                rightJoystick.top -> -intakeSpeedScalar * 1.0
                else -> 0.0
            })
            Control.Controller -> {
                val leftSpeed = controller.getTriggerAxis(GenericHID.Hand.kLeft)
                val rightSpeed = controller.getTriggerAxis(GenericHID.Hand.kRight)

                // Support variable intake speeds through self-cancellation
                Climber.set(leftSpeed - rightSpeed)
            }
        }

        return false
    }

    override fun onDestroy() = Climber.stop()
}