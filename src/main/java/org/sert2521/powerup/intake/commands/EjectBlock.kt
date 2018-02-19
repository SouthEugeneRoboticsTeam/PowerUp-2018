package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sertain.command.Command
import java.util.concurrent.TimeUnit

class EjectBlock : Command(500) {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.set(-intakeSpeedScalar)
        return false
    }

    override fun onDestroy() = Intake.stop()
}
