package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.util.secondaryJoystick
import org.sertain.command.PidCommand

class ElevateToScale : PidCommand(0.0001) {
    private val success: Boolean
        get() = Elevator.topTrigger.get() && Elevator.middleTrigger.get()
    private val abort: Boolean
        get() = secondaryJoystick.trigger

    init {
        requires(Elevator)
    }

    override fun onCreate() {
        setpoint = -3330.0
    }

    override fun returnPidInput() = Elevator.position.toDouble()

    override fun usePidOutput(output: Double) {
        println(output)
//        Elevator.set(output)
    }

    override fun execute() = success || abort

    override fun onDestroy() {
        Elevator.stop()
    }
}
