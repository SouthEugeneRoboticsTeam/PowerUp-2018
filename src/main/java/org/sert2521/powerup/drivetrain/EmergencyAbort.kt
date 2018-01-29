package org.sert2521.powerup.drivetrain

import org.sert2521.powerup.drivetrain.Drivetrain.leftSpeed
import org.sert2521.powerup.drivetrain.Drivetrain.rightSpeed
import org.sertain.command.Command
import kotlin.math.absoluteValue
import kotlin.math.sign

class EmergencyAbort : Command() {
    private var history = listOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)

    override fun execute(): Boolean {
        history += Drivetrain.ahrs.roll
        history = history.takeLast(8)

        val avg = history.average()
        if (avg.absoluteValue >= 16) {
            println("EMERGENCY ABORT! ${avg.absoluteValue}")
            FixedDrive(
                    leftSpeed - 0.01 * -avg.sign,
                    rightSpeed - 0.01 * -avg.sign
            ).start()
        }

        return false
    }
}
