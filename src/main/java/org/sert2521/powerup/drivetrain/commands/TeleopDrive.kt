package org.sert2521.powerup.drivetrain.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator.position
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

    private val speedScalar get() = if (rightJoystick.getRawButton(10)) 1.0 else driveSpeedScalar

    override fun execute(): Boolean {
        val safe: Double.() -> Double = {
            val speed = this * (-(5.5 * 10.0.pow(-8) * (position + 2200)).pow(2) + 1)
            if (position >= 4289) MIN_SPEED else speed
        }

        val acceleration: Double.() -> Double = {
            .3 * rightJoystick.y.pow(3) + .7 * rightJoystick.y
        }

        when (controlMode) {
            is Control.Arcade ->
                Drivetrain.arcade(speedScalar * -rightJoystick.y.safe().acceleration(), rightJoystick.x)
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
        const val MIN_SPEED = 0.17
    }
}
