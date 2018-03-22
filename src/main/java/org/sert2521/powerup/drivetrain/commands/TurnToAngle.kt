package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command
import kotlin.math.sqrt

class TurnToAngle(private val angle: Double) : Command() {
    private val initialLeftPosition = Drivetrain.leftPosition
    private val initialRightPosition = Drivetrain.rightPosition

    init {
        requires(Drivetrain)
    }

    private fun errorToSpeed(error: Double) = sqrt(error - 2)

    override fun execute(): Boolean {
        val leftDelta = (Drivetrain.leftPosition - initialLeftPosition) / FULL_TURN
        val rightDelta = (initialRightPosition - Drivetrain.rightPosition) / FULL_TURN

        val leftError = angle - leftDelta
        val rightError = angle - rightDelta

        Drivetrain.drive(errorToSpeed(leftError), -errorToSpeed(rightError))

        return leftError < ALLOWABLE_ERROR && rightError < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 5.0 // In degrees
        const val FULL_TURN = 43116 // Encoder ticks
    }
}
