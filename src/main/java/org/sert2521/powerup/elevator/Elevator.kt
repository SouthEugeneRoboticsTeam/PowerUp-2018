package org.sert2521.powerup.elevator

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.elevator.commands.SendToBottom
import org.sert2521.powerup.elevator.commands.SendToScale
import org.sert2521.powerup.elevator.commands.SendToSwitch
import org.sert2521.powerup.util.BOTTOM_TRIGGER_PORT
import org.sert2521.powerup.util.LEFT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.MIDDLE_TRIGGER_PORT
import org.sert2521.powerup.util.RIGHT_ELEVATOR_MOTOR
import org.sert2521.powerup.util.SWITCH_TRIGGER_PORT
import org.sert2521.powerup.util.TOP_TRIGGER_PORT
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.encoderPosition
import org.sertain.hardware.invert
import org.sertain.hardware.plus
import org.sertain.hardware.resetEncoder
import org.sertain.hardware.whenActive

object Elevator : Subsystem() {
    private val elevator =
            Talon(RIGHT_ELEVATOR_MOTOR).autoBreak() + Talon(LEFT_ELEVATOR_MOTOR).autoBreak().invert()

    val position get() = elevator.encoderPosition

    val atBottom get() = !Elevator.bottomTrigger.get()
    val atSwitch get() = !Elevator.switchTrigger.get()
    val atTop get() = !Elevator.middleTrigger.get() && !Elevator.topTrigger.get()

    private val bottomTrigger = DigitalInput(BOTTOM_TRIGGER_PORT)
    private val middleTrigger = DigitalInput(MIDDLE_TRIGGER_PORT)
    private val topTrigger = DigitalInput(TOP_TRIGGER_PORT)
    private val switchTrigger = DigitalInput(SWITCH_TRIGGER_PORT)

    override val defaultCommand = Elevate()

    override fun onCreate() {
        elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
        elevator.resetEncoder()
    }

    override fun onStart() {
        secondaryJoystick.whenActive(1, Elevate()) // Ensure drivers can override auto
        secondaryJoystick.whenActive(3, SendToBottom())
        secondaryJoystick.whenActive(4, SendToSwitch())
        secondaryJoystick.whenActive(5, SendToScale())
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
