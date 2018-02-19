package org.sert2521.powerup.elevator

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.elevator.commands.EncoderResetter
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
import org.sertain.hardware.DigitalInput
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.getEncoderPosition
import org.sertain.hardware.invert
import org.sertain.hardware.plus
import org.sertain.hardware.setEncoderPosition
import org.sertain.hardware.setSelectedSensor
import org.sertain.hardware.whenActive

object Elevator : Subsystem() {
    const val DEFAULT_SPEED = 0.1
    const val BOTTOM_TARGET = 0
    const val SWITCH_TARGET = 1000
    const val SCALE_TARGET = 3400
    const val SAFE_MAX_TARGET = 4000

    private val elevator = Talon(RIGHT_ELEVATOR_MOTOR).autoBreak() +
            Talon(LEFT_ELEVATOR_MOTOR).autoBreak().invert()

    val position get() = -elevator.getEncoderPosition()

    val atBottom get() = bottomTrigger.get()
    val atSwitch get() = switchTrigger.get()
    val atTop get() = middleTrigger.get() && topTrigger.get()

    private val bottomTrigger = DigitalInput(BOTTOM_TRIGGER_PORT).invert()
    private val middleTrigger = DigitalInput(MIDDLE_TRIGGER_PORT).invert()
    private val topTrigger = DigitalInput(TOP_TRIGGER_PORT).invert()
    private val switchTrigger = DigitalInput(SWITCH_TRIGGER_PORT).invert()

    override val defaultCommand = Elevate()

    override fun onCreate() {
        elevator.setSelectedSensor(FeedbackDevice.QuadEncoder)
    }

    override fun onStart() {
        EncoderResetter().start()
    }

    override fun onTeleopStart() {
        secondaryJoystick.whenActive(1, Elevate()) // Ensure drivers can override auto
        secondaryJoystick.whenActive(4, SendToBottom())
        secondaryJoystick.whenActive(3, SendToSwitch())
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
        SmartDashboard.putNumber("Elevator Speed", speed)
        elevator.set(speed)
    }

    fun reset() = elevator.setEncoderPosition(0)

    fun stop() {
        elevator.stopMotor()
        set(DEFAULT_SPEED)
    }
}
