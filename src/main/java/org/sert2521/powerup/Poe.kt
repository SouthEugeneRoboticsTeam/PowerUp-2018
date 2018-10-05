package org.sert2521.powerup

import edu.wpi.first.wpilibj.CameraServer
import org.sert2521.powerup.autonomous.Auto
import org.sert2521.powerup.climber.Climber
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.Lights
import org.sert2521.powerup.util.Modes
import org.sert2521.powerup.util.TimeSync
import org.sert2521.powerup.util.UDPServer
import org.sert2521.powerup.util.initPreferences
import org.sert2521.powerup.util.logTelemetry
import org.sertain.Robot

class Poe : Robot() {
    override fun onCreate() {
        Drivetrain
        Intake
        Elevator
        Climber
        Auto
        Modes
        Lights

        UDPServer.start()
        TimeSync.start()
        CameraServer.getInstance().startAutomaticCapture()

        initPreferences()
        logTelemetry()
    }
}
