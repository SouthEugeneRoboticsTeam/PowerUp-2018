package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sertain.command.Command
import kotlin.math.pow
import kotlin.properties.Delegates

abstract class SendToTarget(requireSubsystem: Boolean) : Command(5000) {
    protected abstract val isAtTarget: Boolean
    protected val downSpeed = {
        (DOWN_GRADIENT.pow(1000 / Elevator.position.coerceAtLeast(1)) - MAX_DOWN_POWER)
                .coerceAtMost(MIN_DOWN_POWER)
    }

    init {
        if (requireSubsystem) requires(Elevator)
    }

    override fun execute() = isAtTarget

    override fun onDestroy() = Elevator.stop()

    private companion object {
        const val DOWN_GRADIENT = 1.1
        const val MAX_DOWN_POWER = -0.6
        const val MIN_DOWN_POWER = -0.35
    }
}

class SendToBottom(requireSubsystem: Boolean = true) : SendToTarget(requireSubsystem) {
    override val isAtTarget get() = Elevator.atBottom

    override fun execute(): Boolean {
        Elevator.set(downSpeed())
        return Elevator.atBottom
    }
}

class SendToSwitch(requireSubsystem: Boolean = true) : SendToTarget(requireSubsystem) {
    override val isAtTarget get() = Elevator.atSwitch
    private var wasBelowTarget: Boolean by Delegates.notNull()

    override fun onCreate() {
        wasBelowTarget = Elevator.position <= Elevator.SWITCH_TARGET
    }

    override fun execute(): Boolean {
        Elevator.set(if (wasBelowTarget) 1.0 else downSpeed())
        return super.execute()
    }
}

class SendToScale(requireSubsystem: Boolean = true) : SendToTarget(requireSubsystem) {
    override val isAtTarget get() = Elevator.atTop

    override fun execute(): Boolean {
        Elevator.set(1.0)
        return super.execute()
    }
}
