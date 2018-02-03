package org.sert2521.powerup.paths

import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.angle
import org.sertain.util.generate
import org.sertain.util.split
import org.sertain.util.with
import java.io.File

object MiddleToRight : PathInitializer() {
    private const val MAX_VELOCITY = 0.35
    private const val MAX_ACCEL = 0.075

    private val saveFile = File("MiddleToRight.csv")

    override val trajectory = TrajectoryConfig(MAX_VELOCITY, MAX_ACCEL, 60.0).generate(arrayOf(
            7.5 with .2 angle 0.0,
            4.5 with 1.5 angle 0.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()

    init {
        logGeneratedPoints()

        left.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
        left.configurePIDVA(2.75, 0.0, 0.25, 1 / MAX_VELOCITY, 0.5)

        right.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
        right.configurePIDVA(2.75, 0.0, 0.25, 1 / MAX_VELOCITY, 0.5)
    }
}
