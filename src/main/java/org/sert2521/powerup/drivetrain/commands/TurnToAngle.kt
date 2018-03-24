package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.Command
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

class TurnToAngle(angle: Double) : Command() {
    private var startLeft: Int by Delegates.notNull()
    private var startRight: Int by Delegates.notNull()

    private val turnAmount = (angle / 360) * FULL_TURN

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        startLeft = Drivetrain.leftPosition
        startRight = Drivetrain.rightPosition
    }

    override fun execute(): Boolean {
        val leftDelta = Drivetrain.leftPosition - startLeft
        val rightDelta = startRight - Drivetrain.rightPosition

        val leftError = turnAmount - leftDelta
        val rightError = turnAmount - rightDelta

        Drivetrain.drive(SPEED_FACTOR * leftError, -SPEED_FACTOR * rightError)

        return leftError.absoluteValue < ALLOWABLE_ERROR
                || rightError.absoluteValue < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 100 // Encoder ticks
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION

        const val SPEED_FACTOR = 0.45 * 0.00014
    }
}
