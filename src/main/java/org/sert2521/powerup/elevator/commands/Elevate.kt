package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        Elevator.restrictiveSet(if (secondaryJoystick.trigger) secondaryJoystick.y else 0.0)
        if (secondaryJoystick.getRawButton(5)) ReachToScale().start()
        if (secondaryJoystick.getRawButton(3)) ReachToSwitch().start()
        if (secondaryJoystick.getRawButton(4)) Return().start()
        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
