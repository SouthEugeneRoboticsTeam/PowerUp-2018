package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        val atTop = !Elevator.middleTrigger.get() && !Elevator.topTrigger.get()
        val atBottom = !Elevator.bottomTrigger.get()

        Elevator.set(if (secondaryJoystick.trigger && !atTop && !atBottom) {
            secondaryJoystick.y
        } else {
            0.1
        })

        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
