package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sertain.command.Command
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.properties.Delegates

abstract class SendToTarget(private val target: Int) : Command(5000) {
    protected abstract val isAtTarget: Boolean
    private var wasBelowTarget: Boolean by Delegates.notNull()

    init {
        requires(Elevator)
    }

    override fun onCreate() {
        wasBelowTarget = Elevator.position <= target
    }

    override fun execute(): Boolean {
        // Example: https://www.desmos.com/calculator/l3pczqcg88
        Elevator.set(if (wasBelowTarget) {
            1.0
        } else {
            (DOWN_GRADIENT.pow(1000 / Elevator.position) - MAX_DOWN_POWER)
                    .coerceAtMost(MIN_DOWN_POWER)
        })

        return isAtTarget
    }

    override fun onDestroy() = Elevator.stop()

    private companion object {
        const val DOWN_GRADIENT = 1.2
        const val MAX_DOWN_POWER = -0.3
        const val MIN_DOWN_POWER = -0.2
    }
}

class SendToBottom : SendToTarget(Elevator.BOTTOM_TARGET) {
    override val isAtTarget get() = Elevator.atBottom
}

class SendToSwitch : SendToTarget(Elevator.SWITCH_TARGET) {
    override val isAtTarget get() = Elevator.atSwitch
}

class SendToScale : SendToTarget(Elevator.SCALE_TARGET) {
    override val isAtTarget get() = Elevator.atTop
}
