package org.sert2521.powerup

import org.sert2521.powerup.autonomous.Auto
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Modes
import org.sertain.Robot

class RobotName : Robot() {
    override fun onCreate() {
        Drivetrain
        Intake
        Elevator
        Auto
        Modes
    }
}
