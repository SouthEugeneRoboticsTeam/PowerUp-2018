package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.intake.Intake
import org.sertain.command.Command
import java.util.concurrent.TimeUnit

class Eject : Command(1, TimeUnit.SECONDS) {
    override fun execute(): Boolean {
        Intake.set(-1.0)
        return false
    }
}
