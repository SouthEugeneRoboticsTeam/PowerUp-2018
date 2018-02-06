package org.sert2521.powerup.autonomous

import org.sert2521.powerup.util.MAX_ACCELERATION
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.angle
import org.sertain.util.generate
import org.sertain.util.split
import org.sertain.util.with

private val AUTO_TRAJECTORY_CONFIG = TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION, 60.0)

object CrossBaselinePath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            0.0 with 0.0 angle 0.0,
            2.7 with 0.0 angle 0.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}

object LeftToLeftPath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            7.5 with -3.0 angle 0.0,
            5.2 with -3.5 angle 0.0,
            3.9 with -2.4 angle -100.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}

object MiddleToLeftPath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            7.5 with .2 angle 0.0,
            4.6 with -1.5 angle 0.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}

object MiddleToRightPath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            7.5 with .2 angle 0.0,
            4.8 with 1.6 angle 0.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}

object RightToRightPath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            7.5 with 3.0 angle 0.0,
            5.2 with 3.5 angle 0.0,
            3.8 with 2.5 angle 95.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}

object ReversePath : PathInitializer() {
    override val trajectory = AUTO_TRAJECTORY_CONFIG.generate(arrayOf(
            0.0 with 0.0 angle 0.0,
            0.5 with 0.5 angle 90.0,
            -0.7 with 2.0 angle 90.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}
