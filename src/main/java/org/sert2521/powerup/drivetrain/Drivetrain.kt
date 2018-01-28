package org.sert2521.powerup.drivetrain

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import org.sert2521.powerup.util.LEFT_FRONT_MOTOR
import org.sert2521.powerup.util.LEFT_REAR_MOTOR
import org.sert2521.powerup.util.RIGHT_FRONT_MOTOR
import org.sert2521.powerup.util.RIGHT_REAR_MOTOR
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.leftJoystick
import org.sertain.RobotLifecycle
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus
import org.sertain.hardware.resetEncoder

object Drivetrain : Subsystem(), RobotLifecycle {
    val ahrs = AHRS(I2C.Port.kMXP)

    private val leftDrive =
            Talon(LEFT_FRONT_MOTOR).autoBreak() + Talon(LEFT_REAR_MOTOR).autoBreak()
    private val rightDrive =
            Talon(RIGHT_FRONT_MOTOR).autoBreak() + Talon(RIGHT_REAR_MOTOR).autoBreak()

    private val drive = DifferentialDrive(leftDrive, rightDrive)

    override val defaultCommand = ControllerDrive()

    init {
        leftDrive.autoBreak()
        rightDrive.autoBreak()
    }

    override fun onStart() {
        EmergencyAbort().start()

        stop()

        leftDrive.resetEncoder()
        rightDrive.resetEncoder()
    }

    fun arcadeDrive() {
        drive.arcadeDrive(-Math.pow(leftJoystick.x, 2.0), Math.pow(leftJoystick.y, 2.0))
    }

    fun tank(left: Double, right: Double) {
        drive.tankDrive(left, right)
    }

    fun controllerDrive() {
        val speed = -controller.getY(GenericHID.Hand.kLeft)
        val rotation = controller.getX(GenericHID.Hand.kRight)

        drive.arcadeDrive(speed, rotation)
    }

    fun stop() {
        drive.stopMotor()
    }
}
