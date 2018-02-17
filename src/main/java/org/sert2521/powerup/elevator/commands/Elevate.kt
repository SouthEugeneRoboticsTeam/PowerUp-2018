package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command
import kotlin.math.sign

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        val y = secondaryJoystick.y
        val isNotLeavingExtremities =
                (!Elevator.atTop || y < 0.0) && (!Elevator.atBottom || y > 0.0)
        Elevator.set(if (secondaryJoystick.trigger && isNotLeavingExtremities) {
            if (y.sign < 0) DOWN_SPEED_SCALAR * y else y
        } else {
            0.1
        })

        return false
    }

    override fun onDestroy() = Elevator.stop()

    private companion object {
        const val DOWN_SPEED_SCALAR = 0.6
    }
}
