package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        val atTop = Elevator.middleTrigger.get() && Elevator.topTrigger.get()
        val atBottom = Elevator.bottomTrigger.get()
        if (atTop && secondaryJoystick.y > 0.0)
        if ((atTop && secondaryJoystick.y > 0.0) || (atBottom && secondaryJoystick.y < 0.0)) {
            Elevator.set(if (secondaryJoystick.trigger) secondaryJoystick.y else 0.1)
        }
        if (secondaryJoystick.getRawButton(5)) ElevateToScale().start()
        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
