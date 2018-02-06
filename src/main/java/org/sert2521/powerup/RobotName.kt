package org.sert2521.powerup

import org.sert2521.powerup.autonomous.prepAuto
import org.sert2521.powerup.autonomous.startAuto
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sertain.Robot

class RobotName : Robot() {
    override fun onCreate() {
        Drivetrain
        Intake
        Elevator

        prepAuto()
    }

    override fun onAutoStart() {
        startAuto()
    }
}
