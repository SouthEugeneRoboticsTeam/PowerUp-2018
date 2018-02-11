package org.sert2521.powerup.elevator

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.util.LEFT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.RIGHT_ELEVATOR_MOTOR
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.invert
import org.sertain.hardware.plus

object Elevator : Subsystem() {
    private val elevator = Talon(LEFT_ELEVATOR_MOTOR).invert().autoBreak() +
            Talon(RIGHT_ELEVATOR_MOTOR).autoBreak()

    override val defaultCommand = Elevate()

    fun set(speed: Double) {
        SmartDashboard.putNumber("Elevator speed", speed)
        elevator.set(speed)
    }

    fun stop() = elevator.stopMotor()
}
