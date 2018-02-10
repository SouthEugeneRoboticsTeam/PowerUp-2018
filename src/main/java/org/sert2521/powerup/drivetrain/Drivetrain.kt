package org.sert2521.powerup.drivetrain

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.drivetrain.commands.EmergencyAbort
import org.sert2521.powerup.drivetrain.commands.TeleopDrive
import org.sert2521.powerup.util.LEFT_FRONT_MOTOR
import org.sert2521.powerup.util.LEFT_REAR_MOTOR
import org.sert2521.powerup.util.RIGHT_FRONT_MOTOR
import org.sert2521.powerup.util.RIGHT_REAR_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.encoderPosition
import org.sertain.hardware.plus
import org.sertain.hardware.resetEncoder

/**
 * The robot's primary drive base.
 */
object Drivetrain : Subsystem() {
    val ahrs = AHRS(I2C.Port.kMXP)

    val leftSpeed get() = leftDrive.get()
    val rightSpeed get() = leftDrive.get()

    val leftPosition get() = -leftDrive.encoderPosition
    val rightPosition get() = rightDrive.encoderPosition

    private val leftDrive =
            Talon(LEFT_FRONT_MOTOR).autoBreak() + Talon(LEFT_REAR_MOTOR).autoBreak()
    private val rightDrive =
            Talon(RIGHT_FRONT_MOTOR).autoBreak() + Talon(RIGHT_REAR_MOTOR).autoBreak()
    private val drive = DifferentialDrive(leftDrive, rightDrive)

    override val defaultCommand = TeleopDrive()

    override fun onStart() {
        EmergencyAbort().start()

        leftDrive.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000)
        rightDrive.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000)

        leftDrive.resetEncoder()
        rightDrive.resetEncoder()
    }

    override fun execute() {
        SmartDashboard.putNumber("Drivetrain left encoder position", leftPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain right encoder position", rightPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain pitch", ahrs.pitch.toDouble())
        SmartDashboard.putNumber("Drivetrain roll", ahrs.roll.toDouble())
        SmartDashboard.putNumber("Drivetrain yaw", ahrs.yaw.toDouble())
    }

    fun resetEncoders() {
        leftDrive.resetEncoder()
        rightDrive.resetEncoder()
    }

    fun arcade(speed: Double, rotation: Double) {
        logArcade(speed, rotation)
        drive.arcadeDrive(speed, rotation)
    }

    fun curvature(speed: Double, rotation: Double, quickTurn: Boolean) {
        logArcade(speed, rotation, quickTurn)
        drive.curvatureDrive(speed, rotation, quickTurn)
    }

    fun tank(left: Double, right: Double) {
        logTank(left, right)
        drive.tankDrive(left, -right)
    }

    fun drive(left: Double, right: Double) {
        logTank(left, right)
        leftDrive.set(left)
        rightDrive.set(-right)
    }

    fun stop() = drive.stopMotor()

    private fun logArcade(speed: Double, rotation: Double, quickTurn: Boolean? = null) {
        SmartDashboard.putNumber("Drivetrain speed", speed)
        SmartDashboard.putNumber("Drivetrain rotation", rotation)
        quickTurn?.let { SmartDashboard.putBoolean("Drivetrain quick turn", it) }
    }

    private fun logTank(left: Double, right: Double) {
        SmartDashboard.putNumber("Drivetrain left speed", left)
        SmartDashboard.putNumber("Drivetrain right speed", right)
    }
}
