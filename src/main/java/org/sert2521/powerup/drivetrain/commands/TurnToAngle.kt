package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.PidCommand

class TurnToAngle(private val angle: Double) : PidCommand(1.0) {

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        setpoint = Drivetrain.leftPosition + (angle / 360 * FULL_TURN)
    }

    override fun execute(output: Double): Boolean {
        Drivetrain.drive(output, -output)
        return setpoint - Drivetrain.leftPosition < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = Drivetrain.leftPosition.toDouble()

    private companion object {
        const val ALLOWABLE_ERROR = 100.00
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
