package org.sert2521.powerup.intake

import org.sert2521.powerup.util.*
import org.sertain.RobotLifecycle
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.inverted
import org.sertain.hardware.plus

object Intake : Subsystem(), RobotLifecycle {
    private val intake =
            Talon(LEFT_INTAKE_MOTOR).inverted().autoBreak() + Talon(RIGHT_INTAKE_MOTOR).inverted().autoBreak()

    override val defaultCommand = RunIntake()

    override fun onStart() {
        stop()
    }

    fun run() {
        val speed = (leftJoystick.throttle - 1) / 2
        var multiplier = 0

        if (leftJoystick.getRawButton(1)) multiplier = 1
        else if (leftJoystick.getRawButton(2)) multiplier = -1

        intake.set(speed * multiplier)
    }

    fun stop() {
        intake.stopMotor()
    }
}
