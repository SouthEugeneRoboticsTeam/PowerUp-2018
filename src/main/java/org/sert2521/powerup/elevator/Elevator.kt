package org.sert2521.powerup.elevator

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.util.BOTTOM_SWITCH
import org.sert2521.powerup.util.LEFT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.MIDDLE_SWITCH
import org.sert2521.powerup.util.RIGHT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.SadFlimsyEncoder
import org.sert2521.powerup.util.TOP_SWITCH_1
import org.sert2521.powerup.util.TOP_SWITCH_2
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.invert
import org.sertain.hardware.plus

object Elevator : Subsystem() {
    private val elevator = Talon(LEFT_ELEVATOR_MOTOR).invert().autoBreak() +
            Talon(RIGHT_ELEVATOR_MOTOR).autoBreak()

    override val defaultCommand = Elevate()

    val topSwitch1 = DigitalInput(TOP_SWITCH_1)
    val topSwitch2 = DigitalInput(TOP_SWITCH_2)
    val middleSwitch = DigitalInput(MIDDLE_SWITCH)
    val bottomSwitch = DigitalInput(BOTTOM_SWITCH)

    val encoder = SadFlimsyEncoder(9, elevator)

    private const val SPEED_FACTOR = 0.5

    fun set(speed: Double) {
        SmartDashboard.putNumber("Elevator speed", speed)
        elevator.set(speed)
    }

    fun restrictiveSet(speed: Double) {
        if (!((topSwitch1.get() && topSwitch2.get() && speed > 0.0)
                        || (bottomSwitch.get() && speed < 0.0))) set(speed)
    }

    fun stop() = elevator.stopMotor()
}
