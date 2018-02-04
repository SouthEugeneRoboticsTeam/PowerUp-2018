package org.sert2521.powerup

import org.sert2521.powerup.autonomous.initAuto
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

        initAuto()
    }

    override fun execute() {
        println("Left: ${Drivetrain.leftPosition}, Right: ${Drivetrain.rightPosition}")
    }

    override fun onAutoStart() {
        startAuto()
    }
}
