package org.sert2521.powerup.autonomous

import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.SendableBase
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import org.sert2521.powerup.util.MAX_ACCELERATION
import org.sert2521.powerup.util.MAX_JERK
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sertain.RobotLifecycle
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.angle
import org.sertain.util.generate
import org.sertain.util.split
import org.sertain.util.with
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

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

abstract class PathBase : PathInitializer(), RobotLifecycle {
    protected abstract var points: Array<Waypoint>

    private val trajectoryConfig = TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK)
    private val defaultTrajectory by lazy { newTrajectory() }
    private val defaultFollowers by lazy { newFollower() }
    private var liveTrajectory: Trajectory? = null
    private var liveFollowers: Pair<EncoderFollower, EncoderFollower>? = null

    private val lock = ReentrantReadWriteLock()
    override val trajectory get() = lock.read { liveTrajectory } ?: defaultTrajectory
    override val followers get() = lock.read { liveFollowers } ?: defaultFollowers

    private var sendableEntry: NetworkTableEntry? = null
    private var latestEntry: String? = null

    private val pathName: String = javaClass.simpleName
    private val humanReadablePoints
        get() = points.joinToString(" __ ") {
            listOf(it.x, it.y, Math.toDegrees(it.angle)).joinToString()
        }

    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        executor.execute {
            println("Generating path for $pathName...")
            followers // Force initialization
            logGeneratedPoints()
            println("Path generation complete for $pathName.")
        }

        object : SendableBase() {
            init {
                name = pathName
            }

            override fun initSendable(builder: SendableBuilder) {
                builder.setSmartDashboardType("String Chooser")
                sendableEntry = builder.getEntry("selected").also {
                    it.setDefaultString(humanReadablePoints)
                }
            }
        }
    }

    override fun executeDisabled() {
        val current = sendableEntry?.getString(null).let {
            if (it.isNullOrBlank()) null else it
        }
        if (latestEntry == current) return
        latestEntry = current

        try {
            this.points = current!!.replace(" ", "").split("__").map {
                val flatPoint = it.split(",")
                check(flatPoint.size == 3) { "Points must contain an x, y, and angle component." }
                Waypoint(
                        flatPoint[0].toDouble(),
                        flatPoint[1].toDouble(),
                        Pathfinder.d2r(flatPoint[2].toDouble())
                )
            }.toTypedArray()

            executor.execute {
                println("Generating path for $pathName...")
                println("Using points: $humanReadablePoints")
                lock.write {
                    liveTrajectory = newTrajectory()
                    liveFollowers = newFollower()
                }
                logGeneratedPoints()
                println("Path generation complete for $pathName.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            lock.write {
                liveTrajectory = null
                liveFollowers = null
            }
        }
    }

    private fun newTrajectory(): Trajectory {
        fun hash() = sha256(
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

        val pathFile = File(ROOT, "${hash()}.csv")
        return if (pathFile.exists()) {
            Pathfinder.readFromCSV(pathFile)
        } else {
            trajectoryConfig.generate(points).apply {
                if (ROOT.exists() || ROOT.mkdirs()) Pathfinder.writeToCSV(pathFile, this)
            }
        }
    }

    private fun newFollower() = TankModifier(trajectory, WHEELBASE_WIDTH).split()

    private companion object {
        val ROOT = File("/home/lvuser/paths")
        val executor: Executor = Executors.newSingleThreadExecutor()
    }
}

object CrossBaselinePath : PathBase() {
    override var points = arrayOf(
            0.0 with 0.0 angle 0.0,
            2.7 with 0.0 angle 0.0
    )
}

object LeftToLeftSwitchPath : PathBase() {
    override var points = arrayOf(
            0.0 with 3.0 angle 0.0,
            2.3 with 3.3 angle 0.0,
            3.5 with 1.65 angle 90.0
    )
}

object RightToRightSwitchPath : PathBase() {
    override var points = arrayOf(
            0.0 with -3.0 angle 0.0,
            2.0 with -3.2 angle 0.0,
            3.3 with -1.9 angle -90.0
    )
}

object MiddleToLeftSwitchPath : PathBase() {
    override var points = arrayOf(
            0.0 with 0.2 angle 0.0,
            3.0 with 1.3 angle -10.0
    )
}

object MiddleToRightSwitchPath : PathBase() {
    override var points = arrayOf(
            0.0 with 0.2 angle 0.0,
            2.75 with -0.5 angle 10.0
    )
}

object LeftToLeftScalePath : PathBase() {
    override var points = arrayOf(
            0.0 with 3.0 angle 0.0,
            4.2 with 3.0 angle 0.0,
            6.6 with 2.3 angle -20.0
    )
}

object RightToRightScalePath : PathBase() {
    override var points = arrayOf(
            0.0 with -3.0 angle 0.0,
            4.2 with -3.0 angle 0.0,
            6.6 with -2.2 angle 20.0
    )
}

object LeftSwitchToRearPath : PathBase() {
    override var points = arrayOf(
            4.0 with -2.0 angle -90.0,
            3.0 with -2.7 angle 0.0,
            2.3 with -1.7 angle 180.0
    )
}

object RightSwitchToRearPath : PathBase() {
    override var points = arrayOf(
            4.0 with 2.0 angle 90.0,
            3.0 with 2.6 angle 0.0,
            2.3 with 1.6 angle -185.0
    )
}

object ScaleLeftToLeftSwitchPath : PathBase() {
    override var points = arrayOf(
            6.6 with 2.2 angle 0.0,
            5.5 with 2.0 angle 0.0
    )
}

object ScaleRightToRightSwitchPath : PathBase() {
    override var points = arrayOf(
            6.6 with -2.2 angle 0.0,
            5.5 with -2.0 angle 0.0
    )
}

object TestLeftPath : PathBase() {
    override var points = arrayOf(
            0.0 with 0.0 angle 0.0,
            2.0 with 2.0 angle 90.0
    )
}

object TestRightPath : PathBase() {
    override var points = arrayOf(
            0.0 with 0.0 angle 0.0,
            2.0 with -2.0 angle -90.0
    )
}
