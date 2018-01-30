package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command

/**
 * Drives at a specified fixed speed.
 */
class FixedDrive : Command {
    private val leftSpeed: Double
    private val rightSpeed: Double

    /**
     * Sets both sides of the drivetrain to the same [speed].
     *
     * @param speed the speed at which to set both sides of the drivetrain
     */
    constructor(speed: Double) : super() {
        leftSpeed = speed
        rightSpeed = speed
    }

    /**
     * Sets the left and right sides of the drivetrain to different speeds.
     *
     * @param leftSpeed the speed at which to set the left side of the drivetrain
     * @param rightSpeed the speed at which to set the right side of the drivetrain
     */
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
