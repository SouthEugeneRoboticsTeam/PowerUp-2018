package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import kotlin.math.absoluteValue

class TurnToAngle(private val angle: Double) : AngleDriver(1.0) {
    private val startAngle by lazy { Drivetrain.ahrs.yaw }

    override fun onCreate() {
        setpoint = startAngle + angle
    }

    override fun execute(output: Double): Boolean {
        Drivetrain.drive(output, -output)
        return (Drivetrain.ahrs.yaw - startAngle - angle).absoluteValue < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 5.0 // In degrees
    }
}
