package org.sert2521.powerup.autonomous

import jaci.pathfinder.Pathfinder
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.paths.CrossBaseline
import org.sert2521.powerup.paths.LeftToLeft
import org.sert2521.powerup.paths.MiddleToLeft
import org.sert2521.powerup.paths.MiddleToRight
import org.sert2521.powerup.paths.RightToRight
import org.sert2521.powerup.util.Auto
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sert2521.powerup.util.autoMode
import org.sertain.command.Command
import org.sertain.command.then
import org.sertain.util.PathInitializer
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import kotlin.math.PI

private val executor: Executor = ForkJoinPool()

@Deprecated("Remove when reading from files")
fun initAuto() {
//    executor.execute { CrossBaseline }
//    executor.execute { LeftToLeft }
//    executor.execute { RightToRight }
    executor.execute { MiddleToLeft }
//    executor.execute { MiddleToRight }
}

fun startAuto() {
    println("Following: $autoMode")
    FollowPathAndDeliverCube(when (autoMode) {
        Auto.CrossBaseline -> CrossBaseline
        Auto.LeftToLeft -> LeftToLeft
        Auto.RightToRight -> RightToRight
        Auto.MiddleToLeft -> MiddleToLeft
        Auto.MiddleToRight -> MiddleToRight
    })
}

class PathFollower(private val path: PathInitializer) : Command() {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        Drivetrain.resetEncoders()
        path.reset()
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
        println("Doing straight")
        var leftSpeed = 0.0
        var rightSpeed = 0.0

        if (Drivetrain.leftPosition < encoderTicks) {
            leftSpeed = speed
        }

        if (Drivetrain.rightPosition < encoderTicks) {
            rightSpeed = speed
        }

        Drivetrain.drive(leftSpeed, rightSpeed)

        return Drivetrain.leftPosition > encoderTicks && Drivetrain.rightPosition > encoderTicks
    }
}

fun FollowPathAndDeliverCube(path: PathInitializer) {
    println("Init")
    (PathFollower(path) then DriveStraight(-0.25, -2.0)).start()
}
