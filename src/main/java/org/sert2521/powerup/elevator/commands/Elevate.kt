package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command
import org.sertain.hardware.PovButton
import org.sertain.hardware.povButton
import kotlin.math.sign

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
        val isNotLeavingExtremities =
                (!Elevator.atTop || y < 0.0) && (!Elevator.atBottom || y > 0.0)
        Elevator.set(if (secondaryJoystick.trigger && isNotLeavingExtremities) {
            if (y.sign < 0) DOWN_SPEED_SCALAR * y else y
        } else {
            if (!Elevator.atBottom) Elevator.DEFAULT_SPEED else 0.0
        })
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
