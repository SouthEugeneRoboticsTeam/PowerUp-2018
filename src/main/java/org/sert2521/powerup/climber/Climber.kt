package org.sert2521.powerup.climber

import org.sert2521.powerup.climber.commands.Climb
import org.sert2521.powerup.util.LEFT_CLIMBER_MOTOR
import org.sert2521.powerup.util.RIGHT_CLIMBER_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus

object Climber : Subsystem() {
    private val climber =
            Talon(LEFT_CLIMBER_MOTOR).autoBreak() + Talon(RIGHT_CLIMBER_MOTOR).autoBreak()

    override val defaultCommand = Climb()

    fun set(speed: Double) = climber.set(speed)

    fun stop() = climber.stopMotor()
}
