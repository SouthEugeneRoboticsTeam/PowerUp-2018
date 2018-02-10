package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue

class DriveToAngle(private val angle: Double, private val baseSpeed: Double = 0.0) :
        PidCommand(P, I, D) {
    private val startAngle by lazy { Drivetrain.ahrs.yaw }

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        setpoint = startAngle + angle
    }

    override fun execute() =
            (Drivetrain.ahrs.yaw - startAngle - angle).absoluteValue < ALLOWABLE_ERROR

    override fun returnPidInput() = Drivetrain.ahrs.yaw.toDouble()

    override fun usePidOutput(output: Double) =
            Drivetrain.drive(baseSpeed + output, baseSpeed - output)

    private companion object {
        const val P = 0.1
        const val I = 0.0
        const val D = 0.0

        const val ALLOWABLE_ERROR = 7.5 // In degrees
    }
}
