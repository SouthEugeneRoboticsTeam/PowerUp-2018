package org.sert2521.powerup.autonomous.paths

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

object RightToRight : PathInitializer() {
    override val trajectory = TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION, 60.0).generate(arrayOf(
            7.5 with 3.0 angle 0.0,
            5.2 with 3.5 angle 0.0,
            3.8 with 2.5 angle 95.0
    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()
}
