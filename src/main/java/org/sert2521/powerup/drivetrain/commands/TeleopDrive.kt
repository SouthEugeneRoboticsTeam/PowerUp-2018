package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
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

    private val speedScalar get() = driveSpeedScalar

    override fun execute(): Boolean {
        val driveSpeed = 1.0
        SmartDashboard.putNumber("Drive Speed", driveSpeed)
        val safe: Double.() -> Double = {
            driveSpeed * this * (GRADIENT.pow(
                    Elevator.SCALE_TARGET / (Elevator.SAFE_MAX_TARGET - Elevator.position)
            ) + MIN_SPEED)
        }

        when (controlMode) {
            is Control.Arcade ->
                Drivetrain.arcade(speedScalar * -rightJoystick.y.safe(), rightJoystick.x)
            is Control.Curvature -> Drivetrain.curvature(
                    speedScalar * -rightJoystick.y.safe(),
                    rightJoystick.x,
                    rightJoystick.top
            )
            is Control.Tank -> Drivetrain.tank(
                    speedScalar * leftJoystick.y.safe(),
                    speedScalar * rightJoystick.y.safe()
            )
            is Control.Controller -> Drivetrain.arcade(
                    speedScalar * -controller.getY(GenericHID.Hand.kLeft).safe(),
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
