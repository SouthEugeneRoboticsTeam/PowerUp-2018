package org.sert2521.powerup.climber.commands

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
