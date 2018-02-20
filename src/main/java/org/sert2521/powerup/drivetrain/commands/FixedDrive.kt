package org.sert2521.powerup.drivetrain.commands

import org.sert2521.powerup.drivetrain.Drivetrain
import org.sertain.command.Command

/**
 * Drives at a specified fixed speed.
 */
class FixedDrive : Command {
    private val leftSpeed: Double
    private val rightSpeed: Double
    private val continuous: Boolean

    /**
     * Sets both sides of the drivetrain to the same [speed].
     *
     * @param speed the speed at which to set both sides of the drivetrain
     * @param continuous whether the command should be run continuously until interrupted
     */
    constructor(speed: Double, timeout: Long? = null) : super(timeout) {
        leftSpeed = speed
        rightSpeed = speed
        this.continuous = timeout == null
    }

    /**
     * Sets the left and right sides of the drivetrain to different speeds.
     *
     * @param leftSpeed the speed at which to set the left side of the drivetrain
     * @param rightSpeed the speed at which to set the right side of the drivetrain
     * @param continuous whether the command should be run continuously until interrupted
     */
    constructor(leftSpeed: Double, rightSpeed: Double, timeout: Long? = null) : super(timeout) {
        this.leftSpeed = leftSpeed
        this.rightSpeed = rightSpeed
        this.continuous = timeout == null
    }

    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        Drivetrain.drive(leftSpeed, rightSpeed)
        return !continuous
    }
}
