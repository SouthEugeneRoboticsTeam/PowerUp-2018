package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command


class ReachToScale : Command() {
    private val success: Boolean
            get() = Elevator.topSwitch1.get() && Elevator.topSwitch2.get()
    private val abort: Boolean
            get() = secondaryJoystick.trigger

    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        Elevator.set(1.0)
        return success || abort
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}