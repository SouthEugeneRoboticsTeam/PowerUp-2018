package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sertain.hardware.PovButton
import org.sertain.hardware.povButton
import kotlin.math.sign

class Elevate : Command() {
    init {
        requires(Elevator)
    }

    override fun execute(): Boolean {
        updateManualElevatorPower()
//        updateAutoElevatorPower()

        return false
    }

    private fun updateManualElevatorPower() {
        val y = secondaryJoystick.y
        val elevatorSpeed = 1.0
        SmartDashboard.putNumber("Elevator speed", elevatorSpeed)
        val isNotLeavingExtremities =
                (!Elevator.atTop || y < 0.0) && (!Elevator.atBottom || y > 0.0)
        Elevator.set (if (secondaryJoystick.trigger && isNotLeavingExtremities) {
            if (y.sign < 0) DOWN_SPEED_SCALAR * y * elevatorSpeed else y * elevatorSpeed / 2
        } else {
            Elevator.DEFAULT_SPEED
        })
    }

    /*private fun updateAutoElevatorPower() {
        val currentPov = secondaryJoystick.povButton

        if (currentPov == PovButton.CENTER) return

        when (currentPov) {
            PovButton.TOP -> SendToScale()
            PovButton.BOTTOM -> SendToBottom()
            else -> SendToSwitch()
        }.start()
    }*/

    override fun onDestroy() = Elevator.stop()



    private companion object {
        const val DOWN_SPEED_SCALAR = 0.3
    }
}
