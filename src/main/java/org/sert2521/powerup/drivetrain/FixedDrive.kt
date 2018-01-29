package org.sert2521.powerup.drivetrain

import org.sertain.command.Command

class FixedDrive : Command {
    private val leftSpeed: Double
    private val rightSpeed: Double

    constructor(speed: Double) : super() {
        leftSpeed = speed
        rightSpeed = speed
    }

    constructor(leftSpeed: Double, rightSpeed: Double) : super() {
        this.leftSpeed = leftSpeed
        this.rightSpeed = rightSpeed
    }

    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.tank(leftSpeed, rightSpeed)
        return true
    }
}
