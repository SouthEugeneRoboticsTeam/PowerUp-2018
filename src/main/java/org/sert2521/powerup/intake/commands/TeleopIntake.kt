package org.sert2521.powerup.intake.commands

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
class TeleopIntake : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        when (controlMode) {
            is Control.Arcade, is Control.Tank -> Intake.set(when {
                rightJoystick.trigger -> intakeSpeedScalar
                rightJoystick.top -> -intakeSpeedScalar
                else -> DEFAULT_SPEED
            })
            is Control.Controller -> {
                val leftSpeed = controller.getTriggerAxis(GenericHID.Hand.kLeft)
                val rightSpeed = controller.getTriggerAxis(GenericHID.Hand.kRight)

                // Support variable intake speeds through self-cancellation
                val speed = rightSpeed - leftSpeed
                Intake.set(if (speed == 0.0) DEFAULT_SPEED else speed)
            }
        }

        return false
    }

    override fun onDestroy() = Intake.stop()

    private companion object {
        const val DEFAULT_SPEED = 0.3
    }
}
