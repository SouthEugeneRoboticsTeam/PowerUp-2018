/*
package org.sert2521.powerup.climber.commands

import org.sert2521.powerup.climber.Climber
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.leftJoystick
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Command
import org.sertain.hardware.scaledThrottle

class Climb : Command() {
    init {
        requires(Climber)
    }

    override fun execute(): Boolean {
        Climber.set(when {
            secondaryJoystick.getRawButton(11) -> secondaryJoystick.scaledThrottle
            controlMode is Control.Tank && leftJoystick.getRawButton(11) ->
                leftJoystick.scaledThrottle
            else -> 0.0
        })
        return false
    }

    override fun onDestroy() = Climber.stop()
}
*/
