package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.Command
import org.sertain.command.PidCommand
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.properties.Delegates

private const val MIN_SPEED = 0.25

private fun floorOutput(output: Double) = if (output > 0) {
    output.coerceAtLeast(MIN_SPEED)
} else {
    output.coerceAtMost(-MIN_SPEED)
}

class Turn(angle: Double) : Command() {
    private val turnCommand by lazy {
        val navxBroken = Drivetrain.isNavxBroken
        println("Navx is broken: $navxBroken")
        (if (navxBroken) EncoderTurn(angle) else NavxTurn(angle)).apply { start() }
    }

    override fun execute() = turnCommand.isCompleted
}

private class NavxTurn(private val angle: Double) : AngleDriver(p = 0.005, d = 0.01) {
    private var startAngle: Double by Delegates.notNull()

    override fun onCreate() {
        startAngle = Drivetrain.ahrs.angle
        setpoint = startAngle + angle
    }

    override fun execute(output: Double): Boolean {
        val floored = floorOutput(output)
        Drivetrain.drive(floored, -floored)

        return (Drivetrain.ahrs.angle - startAngle - angle).absoluteValue < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 5.0 // In degrees
    }
}

private class EncoderTurn(private val angle: Double) : PidCommand(p = 0.0000725, d = 0.00007) {
    private var leftStart: Int by Delegates.notNull()
    private var rightStart: Int by Delegates.notNull()

    private val position
        get() = angle.sign * Drivetrain.run {
            (leftStart - leftPosition).absoluteValue + (rightStart - rightPosition).absoluteValue
        } / 2
    private val error get() = setpoint - position

    override fun onCreate() {
        leftStart = Drivetrain.leftPosition
        rightStart = Drivetrain.rightPosition

        setpoint = (angle + angle.sign * 25) / 360 * FULL_TURN
    }

    override fun execute(output: Double): Boolean {
        val floored = floorOutput(output)
        Drivetrain.drive(floored, -floored)

        return error.absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = position

    override fun onDestroy() = Drivetrain.stop()

    private companion object {
        const val ALLOWABLE_ERROR = 400.0
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
