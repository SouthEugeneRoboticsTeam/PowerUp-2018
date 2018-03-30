package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.fastEjectSpeedScalar
import org.sertain.command.Command

class EjectBlock : Command(500) {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.set(-fastEjectSpeedScalar)
        return false
    }

    override fun onDestroy() = Intake.stop()
}
