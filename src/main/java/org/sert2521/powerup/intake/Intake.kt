package org.sert2521.powerup.intake

import org.sert2521.powerup.intake.commands.TeleopClimber
import org.sert2521.powerup.util.LEFT_INTAKE_MOTOR
import org.sert2521.powerup.util.RIGHT_INTAKE_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus

/**
 * The robot's Power Cube intake system, consisting of two sets of fly wheels.
 */
object Intake : Subsystem() {
    private val intake =
            Talon(LEFT_INTAKE_MOTOR).autoBreak() + Talon(RIGHT_INTAKE_MOTOR).autoBreak()

    override val defaultCommand = TeleopClimber()

    fun set(speed: Double) = intake.set(speed)

    fun stop() = intake.stopMotor()
}
