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

@Suppress("FunctionName") // Fake class initializer
fun Turn(angle: Double) = object : Command() {
    override fun execute(): Boolean {
        (if (Drivetrain.isNavxBroken) EncoderTurn(angle) else NavxTurn(angle)).start()
        return true
    }
}

private class NavxTurn(private val angle: Double) : AngleDriver(1.0) {
    private var startAngle: Float by Delegates.notNull()

    override fun onCreate() {
        startAngle = Drivetrain.ahrs.yaw
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

private class EncoderTurn(private val angle: Double) : PidCommand(p = 0.0000725, d = 0.00007) {
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

        setpoint = (angle + angle.sign * 30) / 360 * FULL_TURN
    }

    override fun execute(output: Double): Boolean {
        val hack = if (output > 0) {
            output.coerceAtLeast(MIN_SPEED)
        } else {
            output.coerceAtMost(-MIN_SPEED)
        }
        Drivetrain.drive(hack, -hack)

        return error.absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = position

    override fun onDestroy() = Drivetrain.stop()

    private companion object {
        const val ALLOWABLE_ERROR = 400.0
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION

        const val MIN_SPEED = 0.2
    }
}
