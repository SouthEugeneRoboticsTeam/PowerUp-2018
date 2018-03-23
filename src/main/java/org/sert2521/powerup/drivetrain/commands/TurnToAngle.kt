package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.Command
import java.lang.Math.PI
import java.lang.Math.cbrt

class TurnToAngle(private val angle: Double) : Command() {
    private val initialLeftPosition = Drivetrain.leftPosition
    private val initialRightPosition = Drivetrain.rightPosition

    private fun errorToSpeed(error: Double) = 20 * cbrt(error)

    override fun execute(): Boolean {
        val leftDelta = Drivetrain.leftPosition - initialLeftPosition
        val rightDelta = initialRightPosition - Drivetrain.rightPosition

        val leftError = (angle / 360) * FULL_TURN - leftDelta
        val rightError = (angle / 360) * FULL_TURN - rightDelta

        Drivetrain.drive(errorToSpeed(leftError), -errorToSpeed(rightError))

        return leftError < ALLOWABLE_ERROR && rightError < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 500 // Encoder ticks
        const val FULL_TURN = PI * WHEELBASE_WIDTH / PI * WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
