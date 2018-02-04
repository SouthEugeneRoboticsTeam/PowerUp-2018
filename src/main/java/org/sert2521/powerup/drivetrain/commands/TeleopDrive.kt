package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.leftJoystick
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command

/**
 * Allows for teleoperated drive of the robot.
 */
class TeleopDrive : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        when (controlMode) {
            Control.Arcade -> Drivetrain.arcade(-rightJoystick.y, rightJoystick.x)
            Control.Tank -> Drivetrain.tank(leftJoystick.y, rightJoystick.y)
            Control.Controller -> Drivetrain.arcade(
                    -controller.getY(GenericHID.Hand.kLeft),
                    controller.getX(GenericHID.Hand.kRight)
            )
        }

        return false
    }

    override fun onDestroy() {
        Drivetrain.stop()
    }
}
