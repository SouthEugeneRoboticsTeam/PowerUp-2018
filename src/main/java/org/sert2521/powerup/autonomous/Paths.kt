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
import java.security.MessageDigest

private const val SHA_256 = "SHA-256"
private const val HEX_CHARS = "0123456789abcdef"

private fun sha256(vararg inputs: Any?): String {
    val bytes = MessageDigest.getInstance(SHA_256).digest(inputs.map {
        it.toString().toByteArray().toList()
    }.flatten().toByteArray())
    val result = StringBuilder(bytes.size * 2)

    for (byte in bytes) {
        val i = byte.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

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

    private fun hash() = sha256(
            points.sumByDouble {
                var result = it.x
                result = 31.0 * result + it.y
                result = 31.0 * result + it.angle
                result
            },
            trajectoryConfig.max_velocity,
            trajectoryConfig.max_acceleration,
            trajectoryConfig.max_jerk,
            trajectoryConfig.dt,
            trajectoryConfig.fit,
            trajectoryConfig.sample_count
    )

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

object LeftToLeftSwitchPath : PathBase() {
    override val points = arrayOf(
            0.0 with 3.0 angle 0.0,
            2.3 with 3.5 angle 0.0,
            3.7 with 2.0 angle 90.0
    )
}

object RightToRightSwitchPath : PathBase() {
    override val points = arrayOf(
            0.0 with -3.0 angle 0.0,
            2.3 with -3.5 angle 0.0,
            3.6 with -2.0 angle -90.0
    )
}

object MiddleToLeftSwitchPath : PathBase() {
    override val points = arrayOf(
            0.0 with .2 angle 0.0,
            2.7 with 1.6 angle 0.0
    )
}

object MiddleToRightSwitchPath : PathBase() {
    override val points = arrayOf(
            0.0 with .2 angle 0.0,
            2.9 with -1.5 angle 0.0
    )
}

object RightToRightScalePath : PathBase() {
    override val points = arrayOf(
            0.0 with -3.0 angle 0.0,
            6.2 with -3.1 angle 0.0,
            7.5 with -2.0 angle -90.0
    )
}

object LeftToLeftScalePath : PathBase() {
    override val points = arrayOf(
            0.0 with 3.0 angle 0.0,
            6.2 with 3.1 angle 0.0,
            7.5 with 2.0 angle 90.0
    )
}

object LeftSwitchToRearPath : PathBase() {
    override val points = arrayOf(
            0.0 with 2.0 angle 90.0,
            0.5 with 2.75 angle 0.0,
            1.0 with 0.0 angle -90.0
    )
}

object RightSwitchToRearPath : PathBase() {
    override val points = arrayOf(
            4.0 with 2.0 angle 90.0,
            3.5 with 2.75 angle 0.0,
            2.25 with 1.5 angle -90.0
    )
}

object ScaleLeftToRearPath : PathBase() {
    override val points = arrayOf(
            0.0 with -2.3 angle 90.0,
            1.5 with -3.2 angle 0.0,
            2.6 with -1.8 angle -90.0
    )
}

object ScaleRightToRearPath : PathBase() {
    override val points = arrayOf(
            0.0 with 2.3 angle -90.0,
            1.5 with 3.2 angle 0.0,
            2.6 with 1.8 angle 90.0
    )
}

object SwitchLeftToScalePath : PathBase() {
    override val points = arrayOf(
            3.1 with -1.8 angle -90.0,
            1.5 with -3.7 angle 0.0,
            0.0 with -2.3 angle -90.0
    )
}

object SwitchRightToScalePath : PathBase() {
    override val points = arrayOf(
            3.1 with 1.8 angle 90.0,
            1.5 with 3.7 angle 0.0,
            0.0 with 2.3 angle 90.0
    )
}

object MiddleToScaleLeftPath : PathBase() {
    override val points = arrayOf(
            7.5 with .2 angle 0.0,
            5.5 with -3.5 angle 0.0,
            0.0 with -2.4 angle 0.0
    )
}

object MiddleToScaleRightPath : PathBase() {
    override val points = arrayOf(
            7.5 with .2 angle 0.0,
            5.5 with 3.5 angle 0.0,
            0.0 with 2.4 angle 0.0
    )
}

object TestLeftPath : PathBase() {
    override val points = arrayOf(
            0.0 with 0.0 angle 0.0,
            2.0 with 2.0 angle 90.0
    )
}

object TestRightPath : PathBase() {
    override val points = arrayOf(
            0.0 with 0.0 angle 0.0,
            -2.0 with 2.0 angle -90.0
    )
}
