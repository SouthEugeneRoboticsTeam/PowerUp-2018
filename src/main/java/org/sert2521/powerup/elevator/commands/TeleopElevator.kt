package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command

class TeleopElevator : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        if (controlMode == Control.Arcade || controlMode == Control.Tank) {
            val speed = 0.5
            var multiplier = 0

            if (secondaryJoystick.getRawButton(1)) {
                multiplier = 1
            } else if (secondaryJoystick.getRawButton(2)) {
                multiplier = -1
            }
            Elevator.set(speed * multiplier)
        }
        return false
    }

    override fun onDestroy() {
        Elevator.stop()
    }
}
