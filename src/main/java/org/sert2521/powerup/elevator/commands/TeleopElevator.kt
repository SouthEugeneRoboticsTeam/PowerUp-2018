package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class TeleopElevator : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        if (secondaryJoystick.trigger) Elevator.set(secondaryJoystick.y)
        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
