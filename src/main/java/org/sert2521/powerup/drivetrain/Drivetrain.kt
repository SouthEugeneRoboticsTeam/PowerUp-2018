package org.sert2521.powerup.drivetrain

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.drivetrain.commands.TeleopDrive
import org.sert2521.powerup.util.LEFT_FRONT_MOTOR
import org.sert2521.powerup.util.LEFT_REAR_MOTOR
import org.sert2521.powerup.util.RIGHT_FRONT_MOTOR
import org.sert2521.powerup.util.RIGHT_REAR_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.getEncoderPosition
import org.sertain.hardware.plus
import org.sertain.hardware.setEncoderPosition
import org.sertain.hardware.setSelectedSensor

/**
 * The robot's primary drive base.
 */
object Drivetrain : Subsystem() {
    val ahrs = AHRS(I2C.Port.kMXP)

    val leftPosition get() = -leftDrive.getEncoderPosition()
    val rightPosition get() = rightDrive.getEncoderPosition()

    private val leftDrive =
            Talon(LEFT_FRONT_MOTOR).autoBreak() + Talon(LEFT_REAR_MOTOR).autoBreak()
    private val rightDrive =
            Talon(RIGHT_FRONT_MOTOR).autoBreak() + Talon(RIGHT_REAR_MOTOR).autoBreak()
    private val drive = DifferentialDrive(leftDrive, rightDrive)

    override val defaultCommand = TeleopDrive()

    override fun onCreate() {
        leftDrive.setSelectedSensor(FeedbackDevice.QuadEncoder)
        rightDrive.setSelectedSensor(FeedbackDevice.QuadEncoder)
    }

    override fun onStart() = reset()

    override fun execute() {
        SmartDashboard.putNumber("Drivetrain Left Position", leftPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain Right Position", rightPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain Pitch", ahrs.pitch.toDouble())
        SmartDashboard.putNumber("Drivetrain Roll", ahrs.roll.toDouble())
        SmartDashboard.putData("AHRS", ahrs)
    }

    fun reset() {
        leftDrive.setEncoderPosition(0)
        rightDrive.setEncoderPosition(0)
        ahrs.reset()
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
        SmartDashboard.putNumber("Drivetrain Speed", speed)
        SmartDashboard.putNumber("Drivetrain Rotation", rotation)
        quickTurn?.let { SmartDashboard.putBoolean("Drivetrain Quick Turn", it) }
    }

    private fun logTank(left: Double, right: Double) {
        SmartDashboard.putNumber("Drivetrain Left Speed", left)
        SmartDashboard.putNumber("Drivetrain Right Speed", right)
    }
}
