package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.driveSpeedScalar
import org.sert2521.powerup.util.leftJoystick
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command
import kotlin.math.pow

/**
 * Allows for teleoperated drive of the robot.
 */
class TeleopDrive : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        val safe: Double.() -> Double = {
            this * (GRADIENT.pow(
                    Elevator.SCALE_TARGET / (Elevator.SAFE_MAX_TARGET - Elevator.position)
            ) + MIN_SPEED)
        }

        when (controlMode) {
            is Control.Arcade ->
                Drivetrain.arcade(driveSpeedScalar * -rightJoystick.y.safe(), rightJoystick.x)
            is Control.Curvature -> Drivetrain.curvature(
                    driveSpeedScalar * -rightJoystick.y.safe(),
                    rightJoystick.x,
                    rightJoystick.top
            )
            is Control.Tank -> Drivetrain.tank(
                    driveSpeedScalar * leftJoystick.y.safe(),
                    driveSpeedScalar * rightJoystick.y.safe()
            )
            is Control.Controller -> Drivetrain.arcade(
                    driveSpeedScalar * -controller.getY(GenericHID.Hand.kLeft).safe(),
                    controller.getX(GenericHID.Hand.kRight)
            )
        }

        return false
    }

    override fun onDestroy() = Drivetrain.stop()

    private companion object {
        const val GRADIENT = 0.8
        const val MIN_SPEED = 0.17
    }
}
