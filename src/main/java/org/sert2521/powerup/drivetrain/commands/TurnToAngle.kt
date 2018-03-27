package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue

class TurnToAngle(private val angle: Double) : PidCommand(p = 0.00008, d = 0.00003) {
    init {
        requires(Drivetrain)
    }

    private val encoderValue get() = Drivetrain.leftPosition

    private val error get() = setpoint - encoderValue

    override fun onCreate() {
        setpoint = encoderValue + (angle / 360 * FULL_TURN)
    }

    override fun execute(output: Double): Boolean {
        Drivetrain.drive(output, -output)
        return error.absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput(): Double {
        return Drivetrain.leftPosition.toDouble()
    }

    private companion object {
        const val ALLOWABLE_ERROR = 750.0
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
