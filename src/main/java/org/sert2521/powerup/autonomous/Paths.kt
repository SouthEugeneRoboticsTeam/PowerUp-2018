package org.sert2521.powerup.autonomous

import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import org.sert2521.powerup.util.MAX_ACCELERATION
import org.sert2521.powerup.util.MAX_JERK
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.angle
import org.sertain.util.generate
import org.sertain.util.split
import org.sertain.util.with
import java.io.File

abstract class PathBase : PathInitializer() {
    protected abstract val points: Array<Waypoint>
    override val trajectory: Trajectory by lazy {
        val pathFile = File(ROOT, "${hash()}.csv")
        if (pathFile.exists()) {
            Pathfinder.readFromCSV(pathFile)
        } else {
            trajectoryConfig.generate(points).apply {
                if (ROOT.exists() || ROOT.mkdirs()) Pathfinder.writeToCSV(pathFile, this)
            }
        }
    }
    override val followers by lazy {
        TankModifier(trajectory, WHEELBASE_WIDTH).split()
    }
    private val trajectoryConfig = TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK)

    private fun hash(): String {
        var result = points.sumByDouble {
            fun Double.hashWithSign() = if (this > 0.0) 0.0 else 31.0 + this

            var result = it.x.hashWithSign()
            result = 31.0 * result + it.y.hashWithSign()
            result = 31.0 * result + it.angle.hashWithSign()
            result
        }
        result = 31.0 * result + trajectoryConfig.max_velocity
        result = 31.0 * result + trajectoryConfig.max_acceleration
        result = 31.0 * result + trajectoryConfig.max_jerk
        result = 31.0 * result + trajectoryConfig.dt
        result = 31.0 * result + trajectoryConfig.fit.ordinal.toDouble()
        result = 31.0 * result + trajectoryConfig.sample_count.toDouble()
        return result.toString()
    }

    private companion object {
        val ROOT = File("/home/lvuser/paths")
    }
}

object CrossBaselinePath : PathBase() {
    override val points = arrayOf(
            0.0 with 0.0 angle 0.0,
            2.7 with 0.0 angle 0.0
    )
}

object LeftToLeftPath : PathBase() {
    override val points = arrayOf(
            7.5 with -3.0 angle 0.0,
            5.2 with -3.5 angle 0.0,
            3.9 with -2.4 angle -100.0
    )
}

object MiddleToLeftPath : PathBase() {
    override val points = arrayOf(
            7.5 with .2 angle 0.0,
            4.6 with -1.5 angle 0.0
    )
}

object MiddleToRightPath : PathBase() {
    override val points = arrayOf(
            7.5 with .2 angle 0.0,
            4.8 with 1.6 angle 0.0
    )
}

object RightToRightPath : PathBase() {
    override val points = arrayOf(
            7.5 with 3.0 angle 0.0,
            5.2 with 3.5 angle 0.0,
            3.8 with 2.5 angle 95.0
    )
}

object ReversePath : PathBase() {
    override val points = arrayOf(
            0.0 with 0.0 angle 0.0,
            0.5 with 0.5 angle 90.0,
            -0.7 with 2.0 angle 90.0
    )
}
