package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class Return : Command() {
    private val success: Boolean
            get() = Elevator.bottomTrigger.get()
    private val abort: Boolean
            get() = secondaryJoystick.trigger

    override fun execute(): Boolean {
        Elevator.set(-1.0)
        return success || abort
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
