package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sertain.command.Command

class EncoderResetter : Command() {
    override fun execute(): Boolean {
        if (Elevator.atBottom) Elevator.reset()
        return false
    }
}
