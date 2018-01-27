package org.sert2521.powerup.drivetrain

import edu.wpi.first.wpilibj.drive.DifferentialDrive
import org.sert2521.powerup.util.LEFT_FRONT_MOTOR
import org.sert2521.powerup.util.LEFT_REAR_MOTOR
import org.sert2521.powerup.util.RIGHT_FRONT_MOTOR
import org.sert2521.powerup.util.RIGHT_REAR_MOTOR
import org.sert2521.powerup.util.leftJoystick
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

    override val defaultCommand = ArcadeDrive()

    init {
        leftDrive.autoBreak()
        rightDrive.autoBreak()
    }

    override fun onStart() {
        stop()

        leftDrive.resetEncoder()
        rightDrive.resetEncoder()
    }

    fun arcade() {
        drive.arcadeDrive(-leftJoystick.x, leftJoystick.y)
    }

    fun stop() {
        drive.stopMotor()
    }
}
