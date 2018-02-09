package org.sert2521.powerup.climber

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.climber.commands.Climb
import org.sert2521.powerup.util.FRONT_CLIMBER_MOTOR
import org.sert2521.powerup.util.REAR_CLIMBER_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus

object Climber : Subsystem() {
    private val climber =
            Talon(FRONT_CLIMBER_MOTOR).autoBreak() + Talon(REAR_CLIMBER_MOTOR).autoBreak()

    override val defaultCommand = Climb()

    fun set(speed: Double) {
        SmartDashboard.putNumber("Climber speed", speed)
        climber.set(speed)
    }

    fun stop() = climber.stopMotor()
}
