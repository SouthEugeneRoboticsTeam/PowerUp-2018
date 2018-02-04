package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        Elevator.set(if (secondaryJoystick.trigger) secondaryJoystick.y else 0.0)
        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}