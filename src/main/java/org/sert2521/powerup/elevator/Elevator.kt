package org.sert2521.powerup.elevator

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.climber.Climber
import org.sert2521.powerup.elevator.commands.Elevate
import org.sert2521.powerup.elevator.commands.EncoderResetter
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
import org.sertain.hardware.invert
import org.sertain.hardware.plus
import org.sertain.hardware.setEncoderPosition
import org.sertain.hardware.setSelectedSensor
import org.sertain.hardware.whenActive
import java.util.concurrent.atomic.AtomicBoolean

object Elevator : Subsystem() {
    const val DEFAULT_SPEED = 0.125
    const val SWITCH_TARGET = 1000
    const val SCALE_TARGET = 3400

    private val pdp = PowerDistributionPanel()
    private val elevator = Talon(RIGHT_ELEVATOR_MOTOR).apply {
        autoBreak()
        configContinuousCurrentLimit(40, 0)
        configPeakCurrentLimit(65, 0)
        configPeakCurrentDuration(250, 0)
        enableCurrentLimit(false)
    } + Talon(LEFT_ELEVATOR_MOTOR).apply {
        autoBreak()
        invert()
        configContinuousCurrentLimit(40, 0)
        configPeakCurrentLimit(65, 0)
        configPeakCurrentDuration(250, 0)
        enableCurrentLimit(false)
    }
    val position get() = if (isTripped.get()) SCALE_TARGET else -Climber.getPosition()
    private val current get() = pdp.getCurrent(2)

    val atBottom get() = bottomTrigger.get()
    val atSwitch get() = switchTrigger.get()
    val atTop get() = middleTrigger.get() && topTrigger.get()

    private val bottomTrigger = DigitalInput(BOTTOM_TRIGGER_PORT).invert()
    private val middleTrigger = DigitalInput(MIDDLE_TRIGGER_PORT).invert()
    private val topTrigger = DigitalInput(TOP_TRIGGER_PORT).invert()
    private val switchTrigger = DigitalInput(SWITCH_TRIGGER_PORT).invert()

    private val currents = mutableListOf<Double>()
    private val isTripped = AtomicBoolean()

    override val defaultCommand = Elevate()

    override fun onCreate() = elevator.setSelectedSensor(FeedbackDevice.QuadEncoder)

    override fun onStart() {
        EncoderResetter().start()
        currents.clear()
        currents.addAll(generateSequence { 1.0 }.take(20))
    }

    override fun onTeleopStart() =
            secondaryJoystick.whenActive(1, Elevate()) // Ensure drivers can override auto

    override fun execute() {
        SmartDashboard.putNumber("Elevator Position", position.toDouble())
        SmartDashboard.putBoolean("Elevator breaker tripped", isTripped.get())
        SmartDashboard.putData("Bottom Trigger", bottomTrigger)
        SmartDashboard.putData("Middle Trigger", middleTrigger)
        SmartDashboard.putData("Top Trigger", topTrigger)
        SmartDashboard.putData("Switch Trigger", switchTrigger)
    }

    override fun executeAuto() = executeTeleop()

    override fun executeTeleop() {
        val current = current
        // If there are a ton of 0 current values, we can assume that the breaker tripped since the
        // default elevator speed is _something_.
        if (current > 0 || currents.sum() > 0) {
            if (isTripped.compareAndSet(true, false)) {
                elevator.setEncoderPosition(-SCALE_TARGET)
            }
        } else {
            isTripped.set(true)
        }

        currents.removeAt(0)
        currents.add(current)
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
