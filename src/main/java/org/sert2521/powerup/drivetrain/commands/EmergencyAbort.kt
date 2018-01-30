package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.drivetrain.Drivetrain.leftSpeed
import org.sert2521.powerup.drivetrain.Drivetrain.rightSpeed
import org.sertain.command.Command
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Interrupts the current drivetrain command if the navX angle is above a specified amount in order
 * to prevent the robot from tipping over and dying a tragic death.
 */
class EmergencyAbort : Command() {
    private val tipThreshold = 16
    private val reductionSpeed = 0.01

    private var history = listOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)

    override fun execute(): Boolean {
        history += Drivetrain.ahrs.roll
        history = history.takeLast(8)

        val avg = history.average()
        if (avg.absoluteValue >= tipThreshold) {
            println("EMERGENCY ABORT! ${avg.absoluteValue}")
            // Reduce speed until we aren't dying
            FixedDrive(leftSpeed - reductionSpeed * -avg.sign, rightSpeed - reductionSpeed * -avg.sign).start()
        }

        return false
    }
}
