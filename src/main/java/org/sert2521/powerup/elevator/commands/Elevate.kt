package org.sert2521.powerup.elevator.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
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

    override fun start() {
        SmartDashboard.putNumber("Elevator multiplier", 1.0)
    }

    override fun execute(): Boolean {
        updateManualElevatorPower()
        updateAutoElevatorPower()
        return false
    }

    private fun updateManualElevatorPower() {
        val speedMultiplier = SmartDashboard.getNumber("Elevator multiplier", 1.0)
        val y = secondaryJoystick.y * speedMultiplier
        val isNotLeavingExtremities =
                (!Elevator.atTop || y < 0.0) && (!Elevator.atBottom || y > 0.0)
        Elevator.set(if (secondaryJoystick.trigger && isNotLeavingExtremities) {
            if (y.sign < 0) DOWN_SPEED_SCALAR * y else y
        } else {
            Elevator.DEFAULT_SPEED
        })
    }

    private fun updateAutoElevatorPower() {
        val currentPov = secondaryJoystick.povButton

        if (currentPov == PovButton.CENTER) return

/*        when (currentPov) {
            PovButton.TOP -> SendToScale()
            PovButton.BOTTOM -> SendToBottom()
            else -> SendToSwitch()
        }.start()*/
    }

    override fun onDestroy() = Elevator.stop()

    private companion object {
        const val DOWN_SPEED_SCALAR = 0.6
    }
}
