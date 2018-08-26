package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.intake.Intake
import org.sert2521.powerup.util.DEGREES_PER_PIXEL
import org.sert2521.powerup.util.Vision
import java.util.Date

class DriveToCube : AngleDriver(0.008, 0.0, 0.022) {
    private lateinit var visionLastSeen: Date

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        updateSetpoint(0.0)
        visionLastSeen = Date()
    }

    override fun execute(output: Double): Boolean {
        println("Found Cube: ${Vision.found}, " +
                "X Offset: ${Vision.xOffset}, " +
                "Y Offset: ${Vision.yOffset}, " +
                "Values: (${BASE_SPEED + output}, ${BASE_SPEED - output})")

        if (Vision.found == true) {
            visionLastSeen = Date()

            Drivetrain.drive(BASE_SPEED + output, BASE_SPEED - output)
            updateSetpoint(Vision.xOffset?.toDouble() ?: 0.0 * DEGREES_PER_PIXEL)
            return Intake.hasCube && Elevator.atBottom
        }

        return visionLastSeen.time - Date().time > MAX_TIME_WITHOUT_CUBE
    }

    private fun updateSetpoint(offset: Double) {
        setpoint = Drivetrain.ahrs.angle + offset
    }

    private companion object {
        const val BASE_SPEED = 0.3
        const val MAX_TIME_WITHOUT_CUBE = 5000
    }
}
