package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.ejectSpeedScalar
import org.sertain.command.Command

class EjectBlock : Command(500) {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.set(-ejectSpeedScalar)
        return false
    }

    override fun onDestroy() = Intake.stop()
}
