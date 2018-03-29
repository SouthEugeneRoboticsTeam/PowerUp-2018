package org.sert2521.powerup.drivetrain

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.drivetrain.commands.DriveToCube
import org.sert2521.powerup.drivetrain.commands.TeleopDrive
import org.sert2521.powerup.elevator.commands.SendToBottom
import org.sert2521.powerup.intake.commands.IntakeBlock
import org.sert2521.powerup.util.LEFT_FRONT_MOTOR
import org.sert2521.powerup.util.LEFT_REAR_MOTOR
import org.sert2521.powerup.util.RIGHT_FRONT_MOTOR
import org.sert2521.powerup.util.RIGHT_REAR_MOTOR
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Subsystem
import org.sertain.command.and
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.getEncoderPosition
import org.sertain.hardware.plus
import org.sertain.hardware.setEncoderPosition
import org.sertain.hardware.setSelectedSensor
import org.sertain.hardware.whenActive
import kotlin.math.absoluteValue

/**
 * The robot's primary drive base.
 */
object Drivetrain : Subsystem() {
    private const val MIN_JOYSTICK_TRANSLATION = 0.05

    val ahrs = AHRS(I2C.Port.kMXP)
    val isNavxBroken get() = angles.all { it == angles.first() }
    private val angles = mutableListOf<Double>()

    val leftPosition get() = -leftDrive.getEncoderPosition()
    val rightPosition get() = rightDrive.getEncoderPosition()

    private val leftDrive =
            Talon(LEFT_FRONT_MOTOR).autoBreak() + Talon(LEFT_REAR_MOTOR).autoBreak()
    private val rightDrive =
            Talon(RIGHT_FRONT_MOTOR).autoBreak() + Talon(RIGHT_REAR_MOTOR).autoBreak()
    private val drive = DifferentialDrive(leftDrive, rightDrive)

    private val findCube = SendToBottom() and IntakeBlock() and DriveToCube()

    override val defaultCommand = TeleopDrive()

    override fun onCreate() {
        leftDrive.setSelectedSensor(FeedbackDevice.QuadEncoder)
        rightDrive.setSelectedSensor(FeedbackDevice.QuadEncoder)
    }

    override fun onStart() {
        reset()

        angles.clear()
        angles.addAll(generateSequence(0.0) { it + 1 }.take(50))
    }

    override fun onTeleopStart() {
        rightJoystick.whenActive(12, findCube)
    }

    override fun execute() {
        SmartDashboard.putNumber("Drivetrain Left Position", leftPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain Right Position", rightPosition.toDouble())
        SmartDashboard.putNumber("Drivetrain Pitch", ahrs.pitch.toDouble())
        SmartDashboard.putNumber("Drivetrain Roll", ahrs.roll.toDouble())
        SmartDashboard.putData("AHRS", ahrs)
    }

    override fun executeAuto() {
        updateStoredAngles()
    }

    override fun executeTeleop() {
        if (rightJoystick.run { x.absoluteValue + y.absoluteValue } > MIN_JOYSTICK_TRANSLATION) {
            findCube.cancel()
        }

        updateStoredAngles()
    }

    private fun updateStoredAngles() {
        angles.removeAt(0)
        angles.add(ahrs.angle)
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
