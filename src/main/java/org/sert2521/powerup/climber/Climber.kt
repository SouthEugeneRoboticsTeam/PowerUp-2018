package org.sert2521.powerup.climber

import org.sert2521.powerup.climber.commands.TeleopClimber
import org.sert2521.powerup.util.LEFT_CLIMBER_MOTOR
import org.sert2521.powerup.util.RIGHT_CLIMBER_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus
// rename everything to climber, change to port 1
/**
 * The robot's climber. Controlled by throttle on controller when 11 is pressed
 */
object Climber : Subsystem() {
    private val intake =
            Talon(LEFT_CLIMBER_MOTOR).autoBreak() + Talon(RIGHT_CLIMBER_MOTOR).autoBreak()

    override val defaultCommand = TeleopClimber()

    fun set(speed: Double) = intake.set(speed)

    fun stop() = intake.stopMotor()
}
