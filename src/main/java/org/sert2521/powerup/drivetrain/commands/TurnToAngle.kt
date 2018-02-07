package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue

class TurnToAngle(private val angle: Double, private val baseSpeed: Double = 0.0) :
        PidCommand(P, I, D) {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() = Drivetrain.ahrs.reset()

    override fun execute() = (Drivetrain.ahrs.yaw - angle).absoluteValue < ALLOWABLE_ERROR

    override fun returnPidInput() = Drivetrain.ahrs.yaw.toDouble()

    override fun usePidOutput(output: Double) =
            Drivetrain.drive(baseSpeed + output, baseSpeed - output)

    companion object {
        private const val P = 1.0
        private const val I = 1.0
        private const val D = 1.0

        private const val ALLOWABLE_ERROR = 10 // in degrees
    }
}
