package org.sert2521.powerup.autonomous

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.Pathfinder
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.drivetrain.commands.DriveToAngle
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sert2521.powerup.util.autoMode
import org.sertain.RobotLifecycle
import org.sertain.command.Command
import org.sertain.command.then
import org.sertain.util.PathInitializer
import kotlin.concurrent.thread

object Auto : RobotLifecycle {
    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        thread {
            CrossBaselinePath.logGeneratedPoints()
            LeftToLeftPath.logGeneratedPoints()
            RightToRightPath.logGeneratedPoints()
            MiddleToLeftPath.logGeneratedPoints()
            MiddleToRightPath.logGeneratedPoints()
            LeftToScalePath.logGeneratedPoints()
            RightToScalePath.logGeneratedPoints()
            SwitchLeftToRearPath.logGeneratedPoints()
            SwitchRightToRearPath.logGeneratedPoints()
            println("Done generating paths")
        }
    }

    override fun onAutoStart() {
        println("Following: $autoMode")
//        when (autoMode) {
//            AutoMode.CROSS_BASELINE -> CrossBaseline()
//            AutoMode.LEFT_TO_LEFT -> LeftToLeft()
//            AutoMode.LEFT_TO_SCALE -> LeftToScale()
//            AutoMode.RIGHT_TO_RIGHT -> RightToRight()
//            AutoMode.RIGHT_TO_SCALE -> RightToScale()
//            AutoMode.MIDDLE_TO_LEFT -> MiddleToLeft()
//            AutoMode.MIDDLE_TO_RIGHT -> MiddleToRight()
//        }.start()
        (LeftToLeft() then SwitchLeftToRear() then DriveToAngle(-90.0)).start()
    }
}

private abstract class PathFollowerBase(protected val path: PathInitializer) : Command() {
    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        Drivetrain.reset()
        path.apply {
            reset()

            left.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
            left.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0.0)

            right.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
            right.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0.0)
        }
    }

    override fun execute(): Boolean {
        val leftPosition = Drivetrain.leftPosition
        val rightPosition = Drivetrain.rightPosition

        val angleDiff =
                Pathfinder.boundHalfDegrees(path.heading - Drivetrain.ahrs.angle)
        val turn = TURN_IMPORTANCE * angleDiff
        SmartDashboard.putNumber("Auto turn", turn)
        calculate(leftPosition, rightPosition, turn).apply { drive(first, second) }

        return path.isFinished
    }

    protected open fun calculate(leftPosition: Int, rightPosition: Int, turn: Double) =
            path.left.calculate(leftPosition) - turn to path.right.calculate(rightPosition) + turn

    protected open fun drive(left: Double, right: Double) {
        Drivetrain.drive(left, right)
    }

    private companion object {
        const val TURN_IMPORTANCE = 0.00025
    }
}

private abstract class ReversePathFollowerBase(path: PathInitializer) : PathFollowerBase(path) {
    override fun drive(left: Double, right: Double) = super.drive(-right, -left)

    override fun calculate(
            leftPosition: Int,
            rightPosition: Int,
            turn: Double
    ) = super.calculate(-rightPosition, -leftPosition, -turn)
}

private class CrossBaseline : PathFollowerBase(CrossBaselinePath)

private class LeftToLeft : PathFollowerBase(LeftToLeftPath)

private class LeftToScale : PathFollowerBase(LeftToScalePath)

private class RightToRight : PathFollowerBase(RightToRightPath)

private class RightToScale : PathFollowerBase(RightToScalePath)

private class MiddleToLeft : PathFollowerBase(MiddleToLeftPath)

private class MiddleToRight : PathFollowerBase(MiddleToRightPath)

private class SwitchLeftToRear : ReversePathFollowerBase(SwitchLeftToRearPath)

private class SwitchRightToRear : ReversePathFollowerBase(SwitchRightToRearPath)
