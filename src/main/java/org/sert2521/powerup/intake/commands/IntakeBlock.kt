package org.sert2521.powerup.intake.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.intakeSpeedScalar
import org.sertain.command.Command

class IntakeBlock : Command() {
    init {
        requires(Intake)
    }

    override fun execute(): Boolean {
        Intake.set(intakeSpeedScalar)
        return Intake.hasCube && Elevator.atBottom
    }

    override fun onDestroy() = Intake.stop()
}
