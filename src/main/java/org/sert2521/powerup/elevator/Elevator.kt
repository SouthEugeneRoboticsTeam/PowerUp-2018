package org.sert2521.powerup.elevator

import org.sert2521.powerup.elevator.commands.TeleopElevator
import org.sert2521.powerup.util.LEFT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.RIGHT_ELEVATOR_MOTOR
import org.sertain.RobotLifecycle
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus

object Elevator : Subsystem() {
    private val elevator =
            Talon(LEFT_ELEVATOR_MOTOR).autoBreak() + Talon(RIGHT_ELEVATOR_MOTOR).autoBreak()

    override val defaultCommand = TeleopElevator()

    override fun onStart() = stop()

    fun set(speed: Double) = elevator.set(speed)

    fun stop() = elevator.stopMotor()
}
