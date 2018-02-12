package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sertain.command.Command
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class SendToScale : Command(5, TimeUnit.SECONDS) {
    override fun execute(): Boolean {
        // See https://www.desmos.com/calculator/l3pczqcg88
        Elevator.set(GRADIENT.pow(1000 / (TARGET_POSITION - Elevator.position)) - MAX_POWER)
        return Elevator.atTop
    }

    private companion object {
        const val GRADIENT = 0.85
        const val MAX_POWER = 0.5
        const val TARGET_POSITION = 3333
    }
}
