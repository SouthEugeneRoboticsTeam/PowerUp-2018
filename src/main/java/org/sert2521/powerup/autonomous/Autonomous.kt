package org.sert2521.powerup.autonomous

import jaci.pathfinder.Pathfinder
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.paths.CrossBaseline
import org.sert2521.powerup.paths.LeftToLeft
import org.sert2521.powerup.paths.MiddleToLeft
import org.sert2521.powerup.paths.MiddleToRight
import org.sert2521.powerup.paths.RightToRight
import org.sert2521.powerup.util.autoMode
import org.sertain.command.Command
import org.sertain.util.PathInitializer
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool

private val executor: Executor = ForkJoinPool()

@Deprecated("Remove when reading from files")
fun initAuto() {
    executor.execute { CrossBaseline }
    executor.execute { LeftToLeft }
    executor.execute { RightToRight }
    executor.execute { MiddleToLeft }
    executor.execute { MiddleToRight }
}

fun startAuto() {
    println("Following: $autoMode")
//    PathFollower(when (autoMode) {
//        Auto.CrossBaseline -> CrossBaseline
//        Auto.LeftToLeft -> LeftToLeft
//        Auto.RightToRight -> RightToRight
//        Auto.MiddleToLeft -> MiddleToLeft
//        Auto.MiddleToRight -> MiddleToRight
//     }).start()
    PathFollower(MiddleToRight).start()
}

class PathFollower(private val path: PathInitializer) : Command() {
    init {
        println("Initting pathfollower")
        requires(Drivetrain)
    }

    override fun onCreate() = Drivetrain.resetEncoders()

    override fun execute(): Boolean {
        val leftPosition = -Drivetrain.leftPosition
        val rightPosition = -Drivetrain.rightPosition

        val angleDiff =
                Pathfinder.boundHalfDegrees(Pathfinder.r2d(path.heading) - Drivetrain.ahrs.angle)
        val turn = 0.00025 * angleDiff

        val leftSpeed = path.left.calculate(leftPosition) - turn
        val rightSpeed = path.right.calculate(rightPosition) + turn

        println("left: $leftSpeed, right: $rightSpeed, turn: $turn")
        Drivetrain.drive(leftSpeed, rightSpeed)

        return path.isFinished
    }
}
