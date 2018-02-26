package org.sert2521.powerup.intake.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.fastEjectSpeedScalar
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sert2521.powerup.util.normalEjectSpeedScalar
import org.sert2521.powerup.util.rightJoystick
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

/**
 * This command allows for teleoperated control of the robot's intake.
 */
class TeleopIntake : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.set(when {
            secondaryJoystick.getRawButton(3) -> intakeSpeedScalar
            secondaryJoystick.getRawButton(4) -> -normalEjectSpeedScalar
            secondaryJoystick.getRawButton(6) -> -fastEjectSpeedScalar
            else -> when (controlMode) {
                is Control.Arcade, is Control.Tank -> when {
                    rightJoystick.trigger -> intakeSpeedScalar
                    rightJoystick.top -> -normalEjectSpeedScalar
                    else -> Intake.DEFAULT_SPEED
                }
                is Control.Controller -> {
                    val leftSpeed = controller.getTriggerAxis(GenericHID.Hand.kLeft)
                    val rightSpeed = controller.getTriggerAxis(GenericHID.Hand.kRight)

                    // Support variable intake speeds through self-cancellation
                    val speed = rightSpeed - leftSpeed
                    if (speed == 0.0) Intake.DEFAULT_SPEED else speed
                }
            }
        })

        return false
    }

    override fun onDestroy() = Intake.stop()
}
