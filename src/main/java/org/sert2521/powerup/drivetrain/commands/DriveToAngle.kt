package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.PidCommand
import javax.swing.text.html.HTML.Tag.I
import kotlin.math.absoluteValue

open class DriveToAngle(private var angle: Double, private val baseSpeed: Double = 0.0, p: Double = 1.0, i: Double = 0.0, d: Double = 0.0) :
        PidCommand(p, i, d) {
    private val startAngle by lazy { Drivetrain.ahrs.yaw }

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        setpoint = startAngle + angle
    }

    override fun execute(output: Double): Boolean {
//        println("Actual: ${Drivetrain.ahrs.yaw}")
//        println("Start Angle: $startAngle")
//        println("Angle: $angle")
//        println("Left: ${baseSpeed + output}, Right: ${baseSpeed - output}")
        Drivetrain.drive(baseSpeed + output, baseSpeed - output)
        return (Drivetrain.ahrs.yaw - startAngle - angle).absoluteValue < ALLOWABLE_ERROR
    }

    override fun returnPidInput() = Drivetrain.ahrs.yaw.toDouble()

    override fun onDestroy() = Drivetrain.stop()

    private companion object {
        const val ALLOWABLE_ERROR = 5.0 // In degrees
    }
}
