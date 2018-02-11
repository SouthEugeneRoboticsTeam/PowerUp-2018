package org.sert2521.powerup.elevator.commands

import org.sert2521.powerup.elevator.Elevator
import org.sert2521.powerup.elevator.Elevator.atTop
import org.sert2521.powerup.elevator.Elevator.position
import org.sertain.command.Command
import kotlin.math.pow
import kotlin.math.roundToInt

class SendToScale : Command() {
    private val setPoint = 3330

    fun quadraticCurve(currentPoint: Int): Int {
        return -((currentPoint - setPoint).toDouble().pow(2.0)).roundToInt() //* (currentPoint - setPoint)
    }

    override fun execute(): Boolean {
        Elevator.set(quadraticCurve(position).toDouble())
        println("Do dabbity dough")
        return atTop || position >= 3330
    }
}