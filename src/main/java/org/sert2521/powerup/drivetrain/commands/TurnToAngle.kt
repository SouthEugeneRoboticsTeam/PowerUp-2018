package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command
import java.lang.Math.cbrt

class TurnToAngle(private val angle: Double) : Command() {
    private val initialLeftPosition = Drivetrain.leftPosition
    private val initialRightPosition = Drivetrain.rightPosition

    init {
        requires(Drivetrain)
    }

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
        const val FULL_TURN = 43116 // Encoder ticks
    }
}
