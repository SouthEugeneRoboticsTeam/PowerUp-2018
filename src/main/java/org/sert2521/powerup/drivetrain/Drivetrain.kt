package org.sert2521.powerup.drivetrain

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import org.sert2521.powerup.util.*
import org.sertain.RobotLifecycle
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.inverted
import org.sertain.hardware.plus
import org.sertain.hardware.resetEncoder

object Drivetrain : Subsystem(), RobotLifecycle {
    private val leftDrive =
            Talon(LEFT_FRONT_MOTOR).inverted() + Talon(LEFT_REAR_MOTOR).inverted()
    private val rightDrive =  Talon(RIGHT_FRONT_MOTOR) + Talon(RIGHT_REAR_MOTOR)

    private val drive = DifferentialDrive(leftDrive, rightDrive)

    override val defaultCommand = ControllerDrive()

    init {
        leftDrive.autoBreak()
        rightDrive.autoBreak()
    }

    override fun onStart() {
        stop()

        leftDrive.resetEncoder()
        rightDrive.resetEncoder()
    }

    fun arcadeDrive() {
        drive.arcadeDrive(-leftJoystick.x, leftJoystick.y)
    }

    fun controllerDrive() {
        drive.arcadeDrive(-controller.getX(GenericHID.Hand.kRight), controller.getY(GenericHID.Hand.kLeft))
    }

    fun stop() {
        drive.stopMotor()
    }
}
