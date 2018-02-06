package org.sert2521.powerup.autonomous

import jaci.pathfinder.Pathfinder
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.MAX_ACCELERATION
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEELBASE_WIDTH
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sert2521.powerup.util.autoMode
import org.sertain.command.Command
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.angle
import org.sertain.util.generate
import org.sertain.util.split
import org.sertain.util.with
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import kotlin.math.PI
import kotlin.math.absoluteValue

private val executor: Executor = ForkJoinPool()

@Deprecated("Remove when reading from files")
fun initAuto() {
//    executor.execute { CrossBaseline }
//    executor.execute { LeftToLeft }
//    executor.execute { RightToRight }
//    executor.execute { MiddleToLeft }
//    executor.execute { MiddleToRight }
    executor.execute { Backup }
}

fun startAuto() {
    println("Following: $autoMode")
//    (PathFollower(when (autoMode) {
//        Auto.CrossBaseline -> CrossBaseline
//        Auto.LeftToLeft -> LeftToLeft
//        Auto.RightToRight -> RightToRight
//        Auto.MiddleToLeft -> MiddleToLeft
//        Auto.MiddleToRight -> MiddleToRight
//    }) then PathFollower(Backup)).start()
    PathFollower(Backup).start()
}

class PathFollower(private val path: PathInitializer) : Command() {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        Drivetrain.resetEncoders()
        path.reset()

        path.left.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
        path.left.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0.0)

        path.right.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
        path.right.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0.0)
    }

    override fun execute(): Boolean {
        val leftPosition = Drivetrain.leftPosition
        val rightPosition = Drivetrain.rightPosition

        val angleDiff =
                Pathfinder.boundHalfDegrees(Pathfinder.r2d(path.heading) - Drivetrain.ahrs.angle)
        val turn = 0.00025 * angleDiff

        val leftSpeed = path.left.calculate(leftPosition) - turn
        val rightSpeed = path.right.calculate(rightPosition) + turn

        Drivetrain.drive(leftSpeed, rightSpeed)

        return path.isFinished
    }
}

class DriveStraight(private val speed: Double, distance: Double) : Command() {
    private val encoderTicks: Double

    init {
        requires(Drivetrain)
        encoderTicks = distance / (WHEEL_DIAMETER * PI) * ENCODER_TICKS_PER_REVOLUTION
    }

    override fun onCreate() = Drivetrain.resetEncoders()

    override fun execute(): Boolean {
        var leftSpeed = 0.0
        var rightSpeed = 0.0

        if ((Drivetrain.leftPosition - encoderTicks).absoluteValue > 100) {
            leftSpeed = speed
        }

        if ((Drivetrain.rightPosition - encoderTicks).absoluteValue > 100) {
            rightSpeed = speed
        }

        Drivetrain.drive(leftSpeed, rightSpeed)

        return Drivetrain.leftPosition.absoluteValue > encoderTicks.absoluteValue && Drivetrain.rightPosition.absoluteValue > encoderTicks.absoluteValue
    }
}

object Backup : PathInitializer() {
    override val trajectory = TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION, 60.0).generate(arrayOf(
            0.0 with 0.0 angle 0.0,
            0.5 with 0.5 angle 90.0,
            -0.7 with 2.0 angle 90.0

    ))
    override val followers = TankModifier(trajectory, WHEELBASE_WIDTH).split()

    init {
        logGeneratedPoints()
    }
}

