package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command
import org.sertain.hardware.PovButton
import org.sertain.hardware.povButton

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        updateManualElevatorPower()
        updateAutoElevatorPower()
        return false
    }

    private fun updateManualElevatorPower() {
        val y = secondaryJoystick.y
        if (Elevator.atTop) {
            if (y < 0 && secondaryJoystick.trigger) Elevator.set(y)
            else Elevator.set(Elevator.DEFAULT_SPEED)
        } else if (Elevator.atBottom) {
            if (y > 0 && secondaryJoystick.trigger) Elevator.set(y)
            else 0.0
        } else if (y > Elevator.DEFAULT_SPEED && secondaryJoystick.trigger) Elevator.set(y) else
            if (y < 0 && secondaryJoystick.trigger) Elevator.set(y * DOWN_SPEED_SCALAR)
            else Elevator.set(Elevator.DEFAULT_SPEED)
    }

    private fun updateAutoElevatorPower() {
        val currentPov = secondaryJoystick.povButton

        if (currentPov == PovButton.CENTER) return

        when (currentPov) {
            PovButton.TOP -> SendToScale()
            PovButton.BOTTOM -> SendToBottom()
            else -> SendToSwitch()
        }.start()
    }

    override fun onDestroy() = Elevator.stop()

    private companion object {
        const val DOWN_SPEED_SCALAR = 0.7
    }
}
