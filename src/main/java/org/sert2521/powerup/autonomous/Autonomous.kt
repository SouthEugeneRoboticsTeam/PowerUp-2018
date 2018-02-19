package org.sert2521.powerup.autonomous

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.Pathfinder
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.drivetrain.commands.DriveToAngle
import org.sert2521.powerup.drivetrain.commands.DriveToCube
import org.sert2521.powerup.elevator.commands.SendToBottom
import org.sert2521.powerup.elevator.commands.SendToScale
import org.sert2521.powerup.elevator.commands.SendToSwitch
import org.sert2521.powerup.intake.commands.EjectBlock
import org.sert2521.powerup.intake.commands.IntakeBlock
import org.sert2521.powerup.util.AutoMode
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sert2521.powerup.util.autoMode
import org.sertain.RobotLifecycle
import org.sertain.command.Command
import org.sertain.command.and
import org.sertain.command.then
import org.sertain.util.PathInitializer

object Auto : RobotLifecycle {
    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        TestLeftPath
        TestRightPath

        CrossBaselinePath
        LeftToLeftSwitchPath
        RightToRightSwitchPath
        MiddleToLeftSwitchPath
        MiddleToRightSwitchPath
        LeftToLeftScalePath
        RightToRightScalePath
        LeftSwitchToRearPath
        RightSwitchToRearPath
//        ScaleLeftToRearPath
//        ScaleRightToRearPath
        ScaleLeftToLeftSwitchPath
        ScaleRightToRightSwitchPath
    }

    override fun onAutoStart() {
        println("Following: $autoMode")
        when (autoMode) {
            AutoMode.CROSS_BASELINE -> CrossBaseline()
            AutoMode.LEFT_TO_LEFT_SWITCH -> LeftSwitchToRear()
            AutoMode.RIGHT_TO_RIGHT_SWITCH -> RightToRightSwitch() and SendToSwitch() then
                    EjectBlock() then RightSwitchToRear() and SendToBottom() then DriveToCube()
            AutoMode.MIDDLE_TO_LEFT_SWITCH -> MiddleToLeftSwitch() and SendToSwitch() then
                    EjectBlock()
            AutoMode.MIDDLE_TO_RIGHT_SWITCH -> MiddleToRightSwitch() and SendToSwitch() then
                    EjectBlock()
            AutoMode.LEFT_TO_LEFT_SCALE_PICKUP -> LeftToLeftScale() and SendToSwitch() then
                    SendToScale() then EjectBlock() then
                    DriveToAngle(135.0) then DriveToCube() then IntakeBlock()
            AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP -> RightToRightScale() and SendToSwitch() then
                    SendToScale() then EjectBlock() then
                    DriveToAngle(-135.0) then
                    ScaleRightToRightSwitch() then DriveToCube() then IntakeBlock()
            AutoMode.LEFT_TO_LEFT_SCALE_SWITCH -> LeftToLeftScale() and SendToSwitch() then
                    SendToScale() then EjectBlock() then
                    DriveToAngle(135.0) then
                    ScaleLeftToLeftSwitch() then DriveToCube() then IntakeBlock() then
                    SendToSwitch() then EjectBlock()
            AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH -> RightToRightScale() and SendToSwitch() then
                    SendToScale() then EjectBlock() then
                    DriveToAngle(-135.0) then
                    ScaleRightToRightSwitch() then DriveToCube() then IntakeBlock() then
                    SendToSwitch() then EjectBlock()
            AutoMode.TEST_LEFT -> TestLeft()
            AutoMode.TEST_RIGHT -> TestRight()
        }.also {
            it.start()
        }
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
            left.configurePIDVA(1.0, 0.0, 0.05, 1 / MAX_VELOCITY, 0.0)

            right.configureEncoder(0, ENCODER_TICKS_PER_REVOLUTION, WHEEL_DIAMETER)
            right.configurePIDVA(1.0, 0.0, 0.05, 1 / MAX_VELOCITY, 0.0)
        }
    }

    override fun execute(): Boolean {
        val leftPosition = Drivetrain.leftPosition
        val rightPosition = Drivetrain.rightPosition

        val angleDiff =
                Pathfinder.boundHalfDegrees(path.heading - Drivetrain.ahrs.angle)
        val turn = TURN_IMPORTANCE * angleDiff
        SmartDashboard.putNumber("Auto Turn", turn)
        calculate(leftPosition, rightPosition, turn).apply { drive(first, second) }

        return path.isFinished
    }

    protected open fun calculate(leftPosition: Int, rightPosition: Int, turn: Double) =
            path.left.calculate(leftPosition) - turn to path.right.calculate(rightPosition) + turn

    protected open fun drive(left: Double, right: Double) {
        Drivetrain.drive(left, right)
    }

    private companion object {
        const val TURN_IMPORTANCE = 0.0005
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

private class LeftToLeftSwitch : PathFollowerBase(LeftToLeftSwitchPath)

private class RightToRightSwitch : PathFollowerBase(RightToRightSwitchPath)

private class MiddleToLeftSwitch : PathFollowerBase(MiddleToLeftSwitchPath)

private class MiddleToRightSwitch : PathFollowerBase(MiddleToRightSwitchPath)

private class LeftToLeftScale : PathFollowerBase(LeftToLeftScalePath)

private class RightToRightScale : PathFollowerBase(RightToRightScalePath)

// TODO tune
private class LeftSwitchToRear : ReversePathFollowerBase(LeftSwitchToRearPath)

// TODO tune
private class RightSwitchToRear : ReversePathFollowerBase(RightSwitchToRearPath)

// TODO tune
private class ScaleLeftToLeftSwitch : PathFollowerBase(ScaleLeftToLeftSwitchPath)

// TODO tune
private class ScaleRightToRightSwitch : PathFollowerBase(ScaleRightToRightSwitchPath)

private class TestLeft : PathFollowerBase(TestLeftPath)

private class TestRight : PathFollowerBase(TestRightPath)
