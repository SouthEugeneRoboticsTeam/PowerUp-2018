package org.sert2521.powerup.elevator.commands

import com.sun.org.apache.xerces.internal.dom.DOMNormalizer.abort
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class ReachToSwitch : Command() {
    private val success: Boolean
            get() = Elevator.middleSwitch.get()
    private val abort: Boolean
            get() = secondaryJoystick.trigger

    override fun execute(): Boolean {
        Elevator.set(1.0)
        return success || abort
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}