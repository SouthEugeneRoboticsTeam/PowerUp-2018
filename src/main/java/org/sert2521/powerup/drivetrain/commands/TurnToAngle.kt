package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.command.Command

class TurnToAngle(private val angle: Double) : Command() {
    // TODO: lateinit
    private val initialLeftPosition = Drivetrain.leftPosition
    private val initialRightPosition = Drivetrain.rightPosition

    private val turnAmount = (angle / 360) * FULL_TURN

    init {
        requires(Drivetrain)
        println("Hello there")
    }

    override fun execute(): Boolean {
        val leftDelta = Drivetrain.leftPosition - initialLeftPosition
        val rightDelta = initialRightPosition - Drivetrain.rightPosition

        val leftError = turnAmount - leftDelta
        val rightError = turnAmount - rightDelta

        println("$leftError, $rightError")

        // TODO: Make this value into a P
        // (this is Andrew's sexy version of PID, do not change, working good)
        Drivetrain.drive(0.45 * (leftError * 0.00014), -0.45 * (rightError * 0.00014))

        return leftError < ALLOWABLE_ERROR || rightError < ALLOWABLE_ERROR
    }

    private companion object {
        const val ALLOWABLE_ERROR = 100 // Encoder ticks
        const val FULL_TURN = WHEELBASE_WIDTH / WHEEL_DIAMETER * ENCODER_TICKS_PER_REVOLUTION
    }
}
