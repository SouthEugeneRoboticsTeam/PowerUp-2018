package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sertain.command.Command
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.properties.Delegates

abstract class SendToTarget(private val target: Int) : Command(5, TimeUnit.SECONDS) {
    protected abstract val isAtTarget: Boolean
    private var wasBelowTarget: Boolean by Delegates.notNull()

    init {
        requires(Elevator)
    }

    override fun onCreate() {
        wasBelowTarget = Elevator.position <= target
    }

    override fun execute(): Boolean {
        val gradient = if (wasBelowTarget) UP_GRADIENT else DOWN_GRADIENT
        val maxPower = if (wasBelowTarget) 1 - MAX_UP_POWER else 1 + MAX_DOWN_POWER

        // Example: https://www.desmos.com/calculator/l3pczqcg88
        Elevator.set(if (wasBelowTarget) {
            gradient.pow(1000 / (target - Elevator.position)) - maxPower + (1 - gradient)
        } else {
            gradient.pow(1000 / Elevator.position) - maxPower
        })

        return isAtTarget
    }

    private companion object {
        const val UP_GRADIENT = 0.8
        const val DOWN_GRADIENT = 1.2

        const val MAX_UP_POWER = 0.85
        const val MAX_DOWN_POWER = 0.2
    }
}

class SendToBottom : SendToTarget(0) {
    override val isAtTarget get() = Elevator.atBottom
}

class SendToSwitch : SendToTarget(1000) {
    override val isAtTarget get() = Elevator.atSwitch
}

class SendToScale : SendToTarget(3400) {
    override val isAtTarget get() = Elevator.atTop
}
