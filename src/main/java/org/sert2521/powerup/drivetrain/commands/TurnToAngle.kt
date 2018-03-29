package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.properties.Delegates

class TurnToAngle(private val angle: Double) : PidCommand(p = 0.0000725, d = 0.00007) {
    private var leftStart: Int by Delegates.notNull()
    private var rightStart: Int by Delegates.notNull()

    private val position
        get() = angle.sign * Drivetrain.run {
            (leftStart - leftPosition).absoluteValue + (rightStart - rightPosition).absoluteValue
        } / 2

    private val error get() = setpoint - position

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        leftStart = Drivetrain.leftPosition
        rightStart = Drivetrain.rightPosition

        setpoint = angle / 360 * FULL_TURN
    }

    override fun execute(output: Double): Boolean {
        Drivetrain.drive(output, -output)
        return error.absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = position

    private companion object {
        const val ALLOWABLE_ERROR = 400.0
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
