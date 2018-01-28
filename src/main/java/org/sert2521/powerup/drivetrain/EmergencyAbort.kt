package org.sert2521.powerup.drivetrain

import org.sertain.command.Command
import kotlin.math.absoluteValue

class EmergencyAbort : Command() {
    private var history = listOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)

    override fun execute(): Boolean {
        history += Drivetrain.ahrs.roll.also { println("raw: $it") }
        history = history.takeLast(8)

        val avg = history.average()
        println(avg)
        if (avg.absoluteValue >= 12) {
            FixedDrive(0.0).start()
        }

        return false
    }
}
