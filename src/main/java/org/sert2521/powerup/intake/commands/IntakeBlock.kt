package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sertain.command.Command

class IntakeBlock(private val timeout: Long? = null) : Command(timeout) {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
//        println("Has cube? ${Intake.hasCube}")
        Intake.set(intakeSpeedScalar)
        return Intake.hasCube && timeout == null
    }

    override fun onDestroy() = Intake.stop()
}
