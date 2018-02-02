package org.sert2521.powerup.climber.commands

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Control
import org.sert2521.powerup.util.controlMode
import org.sert2521.powerup.util.controller
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sert2521.powerup.util.rightJoystick
import org.sertain.command.Command

class TeleopClimber : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        if (figureoutjoy.getbutton11.ispressed) Climber.set(joy.scaledtrhottle)
        return false
    }

    override fun onDestroy() = Climber.stop()
}
