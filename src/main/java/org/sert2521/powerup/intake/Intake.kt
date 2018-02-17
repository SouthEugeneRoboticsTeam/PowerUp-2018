package org.sert2521.powerup.intake

import edu.wpi.first.wpilibj.Spark
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.intake.commands.TeleopIntake
import org.sert2521.powerup.util.LEFT_INTAKE_MOTOR
import org.sert2521.powerup.util.RIGHT_INTAKE_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.invert

/**
 * The robot's Power Cube intake system, consisting of two sets of fly wheels.
 */
object Intake : Subsystem() {
    private val left = Spark(LEFT_INTAKE_MOTOR).invert()
    private val right = Spark(RIGHT_INTAKE_MOTOR)

    override val defaultCommand = TeleopIntake()

    fun set(speed: Double) {
        SmartDashboard.putNumber("Intake Speed", speed)
        left.set(speed)
        right.set(speed)
    }

    fun stop() {
        left.stopMotor()
        right.stopMotor()
    }
}
