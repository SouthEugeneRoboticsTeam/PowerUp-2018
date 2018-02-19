package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue

open class DriveToAngle(protected var angle: Double, private val baseSpeed: Double = 0.0) :
        PidCommand(P, I, D) {
    private val startAngle by lazy { Drivetrain.ahrs.yaw }
    protected var adjustedSetpoint
        get() = setpoint - startAngle
        set(value) {
            setpoint = startAngle + value
        }

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        adjustedSetpoint = angle
    }

    override fun execute(output: Double): Boolean {
        adjustedSetpoint = angle
        Drivetrain.drive(baseSpeed + output, baseSpeed - output)
        return (Drivetrain.ahrs.yaw - startAngle - angle).absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = Drivetrain.ahrs.yaw.toDouble()

    private companion object {
        const val P = 0.1
        const val I = 0.0
        const val D = 0.0

        const val ALLOWABLE_ERROR = 7.5 // In degrees
    }
}
