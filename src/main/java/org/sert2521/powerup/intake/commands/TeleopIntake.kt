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
        if (controlMode == Control.Arcade || controlMode == Control.Tank) {
            val speed = (rightJoystick.throttle - 1) / 2
            var multiplier = 0

            if (rightJoystick.getRawButton(1)) multiplier = 1
            else if (rightJoystick.getRawButton(2)) multiplier = -1

            Intake.set(speed * multiplier)
        } else if (controlMode == Control.Controller) {
            val leftSpeed = controller.getTriggerAxis(GenericHID.Hand.kLeft)
            val rightSpeed = controller.getTriggerAxis(GenericHID.Hand.kRight)

            val speed = leftSpeed - rightSpeed

            Intake.set(speed)
        }

        return false
    }

    override fun onDestroy() = Intake.stop()
}
