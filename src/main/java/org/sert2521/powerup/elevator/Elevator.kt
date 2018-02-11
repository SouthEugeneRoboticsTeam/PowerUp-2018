package org.sert2521.powerup.elevator

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.util.BOTTOM_TRIGGER_PORT
import org.sert2521.powerup.util.LEFT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.MIDDLE_TRIGGER_PORT
import org.sert2521.powerup.util.RIGHT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.SWITCH_TRIGGER_PORT
import org.sert2521.powerup.util.TOP_TRIGGER_PORT
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.encoderPosition
import org.sertain.hardware.invert
import org.sertain.hardware.plus
import org.sertain.hardware.resetEncoder

object Elevator : Subsystem() {
    private val elevator =
            Talon(RIGHT_ELEVATOR_MOTOR).autoBreak() + Talon(LEFT_ELEVATOR_MOTOR).autoBreak().invert()

    val position get() = elevator.encoderPosition

    private val bottomTrigger = DigitalInput(BOTTOM_TRIGGER_PORT)
    private val middleTrigger = DigitalInput(MIDDLE_TRIGGER_PORT)
    private val topTrigger = DigitalInput(TOP_TRIGGER_PORT)
    private val switchTrigger = DigitalInput(SWITCH_TRIGGER_PORT)

    val atTop get() = !Elevator.middleTrigger.get() && !Elevator.topTrigger.get()
    val atBottom get() = !Elevator.bottomTrigger.get()

    override val defaultCommand = Elevate()

    override fun onCreate() {
        elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
        elevator.resetEncoder()
    }

    override fun execute() {
        SmartDashboard.putNumber("Elevator Position", position.toDouble())
        SmartDashboard.putData("Bottom Trigger", bottomTrigger)
        SmartDashboard.putData("Middle Trigger", middleTrigger)
        SmartDashboard.putData("Top Trigger", topTrigger)
        SmartDashboard.putData("Switch Trigger", switchTrigger)
    }

    fun set(speed: Double) {
        SmartDashboard.putNumber("Elevator speed", speed)
        elevator.set(speed)
    }

    fun stop() = elevator.stopMotor()
}
