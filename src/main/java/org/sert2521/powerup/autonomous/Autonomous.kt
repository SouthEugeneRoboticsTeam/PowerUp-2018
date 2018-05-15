package org.sert2521.powerup.autonomous

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.followers.EncoderFollower
import org.sert2521.powerup.drivetrain.Drivetrain
import org.sert2521.powerup.drivetrain.commands.DriveStraight
import org.sert2521.powerup.drivetrain.commands.DriveToCube
import org.sert2521.powerup.drivetrain.commands.Turn
import org.sert2521.powerup.elevator.commands.SendToBottom
import org.sert2521.powerup.elevator.commands.SendToScale
import org.sert2521.powerup.elevator.commands.SendToSwitch
import org.sert2521.powerup.intake.commands.EjectBlock
import org.sert2521.powerup.intake.commands.IntakeBlock
import org.sert2521.powerup.util.AutoMode
import org.sert2521.powerup.util.ENCODER_TICKS_PER_REVOLUTION
import org.sert2521.powerup.util.MAX_VELOCITY
import org.sert2521.powerup.util.UDPServer
import org.sert2521.powerup.util.WHEEL_DIAMETER
import org.sert2521.powerup.util.autoMode
import org.sertain.RobotLifecycle
import org.sertain.command.Command
import org.sertain.command.CommandBridgeMirror
import org.sertain.command.and
import org.sertain.command.then
import java.lang.reflect.Field

private var pathProgress: Double? = null

private val shouldEjectBlock = block@{ (pathProgress ?: return@block false) >= 0.85 }
private val isReadyToSendToScale = block@{ (pathProgress ?: return@block false) >= 0.70 }

private fun CommandBridgeMirror.waitUntil(condition: () -> Boolean) = object : Command() {
    override fun execute() = condition()
} then this

object Auto : RobotLifecycle {
    private const val SCALE_TO_SWITCH_TURN = 130.0

    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        TestLeftPath
        TestRightPath

        LeftToLeftSwitchPath
        RightToRightSwitchPath
        MiddleToLeftSwitchPath
        MiddleToRightSwitchPath
        LeftToLeftScalePath
        RightToRightScalePath
        LeftToRightScalePath
        RightToLeftScalePath
        LeftSwitchToRearPath
        RightSwitchToRearPath

        UDPServer
    }

    override fun onAutoStart() {
        println("Following: $autoMode")

        when (autoMode) {
            AutoMode.CROSS_BASELINE -> DriveStraight(0.3, 5000)

            AutoMode.LEFT_TO_LEFT_SWITCH -> LeftToLeftSwitch() and SendToSwitch() then EjectBlock()

            AutoMode.LEFT_TO_LEFT_SWITCH_TWO_CUBE -> LeftToLeftSwitch() and SendToSwitch() then
                    EjectBlock() then LeftSwitchToRear() and SendToBottom() then DriveToCube() and
                    IntakeBlock() then SendToSwitch() then EjectBlock()

            AutoMode.RIGHT_TO_RIGHT_SWITCH -> RightToRightSwitch() and SendToSwitch() then
                    EjectBlock()

            AutoMode.RIGHT_TO_RIGHT_SWITCH_TWO_CUBE -> RightToRightSwitch() and SendToSwitch() then
                    EjectBlock() then RightSwitchToRear() and SendToBottom() then DriveToCube() and
                    IntakeBlock() then SendToSwitch() then EjectBlock()

            AutoMode.MIDDLE_TO_LEFT_SWITCH -> MiddleToLeftSwitch() and SendToSwitch() then
                    EjectBlock()

            AutoMode.MIDDLE_TO_RIGHT_SWITCH -> MiddleToRightSwitch() and SendToSwitch() then
                    EjectBlock()

            AutoMode.LEFT_TO_LEFT_SCALE_PICKUP -> LeftToLeftScale() and SendToScale() and
                    EjectBlock().waitUntil(shouldEjectBlock) then
                    (Turn(SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch())

            AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP -> RightToRightScale() and SendToScale() and
                    EjectBlock().waitUntil(shouldEjectBlock) then
                    (Turn(-SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch())

            AutoMode.LEFT_TO_LEFT_SCALE_SWITCH -> LeftToLeftScale() and SendToScale() and
                    EjectBlock().waitUntil(shouldEjectBlock) then
                    (Turn(SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch() then EjectBlock())

            AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH -> RightToRightScale() and SendToScale() and
                    EjectBlock().waitUntil(shouldEjectBlock) then
                    (Turn(-SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch() then EjectBlock())

            AutoMode.LEFT_TO_RIGHT_SCALE_PICKUP -> LeftToRightScale() and SendToSwitch() and
                    SendToScale(false).waitUntil(isReadyToSendToScale) then
                    EjectBlock() then (Turn(-SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch())

            AutoMode.RIGHT_TO_LEFT_SCALE_PICKUP -> RightToLeftScale() and SendToSwitch() and
                    SendToScale(false).waitUntil(isReadyToSendToScale) then
                    EjectBlock() then (Turn(SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch())

            AutoMode.LEFT_TO_RIGHT_SCALE_SWITCH -> LeftToRightScale() and SendToSwitch() and
                    SendToScale(false).waitUntil(isReadyToSendToScale) then
                    EjectBlock() then (Turn(-SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch() then EjectBlock())

            AutoMode.RIGHT_TO_LEFT_SCALE_SWITCH -> RightToLeftScale() and SendToSwitch() and
                    SendToScale(false).waitUntil(isReadyToSendToScale) then
                    EjectBlock() then (Turn(SCALE_TO_SWITCH_TURN) then DriveToCube()) and
                    (SendToBottom() and IntakeBlock() then SendToSwitch() then EjectBlock())

            AutoMode.TEST_LEFT -> TestLeft()
            AutoMode.TEST_RIGHT -> TestRight()
        }.start()
    }
}

private abstract class PathFollowerBase(protected val path: PathBase) : Command() {
    private val pathIndex get() = pathIndexField.get(path.left) as Int

    init {
        requires(Drivetrain)
    }

    override fun onCreate() {
        pathProgress = 0.0
        Drivetrain.reset() // Needed to ensure second+ paths work
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

        pathProgress = pathIndex.toDouble() / path.trajectory.segments.size.toDouble()

        return path.isFinished
    }

    override fun onDestroy() {
        pathProgress = null
    }

    protected open fun calculate(leftPosition: Int, rightPosition: Int, turn: Double) =
            path.left.calculate(leftPosition) - turn to path.right.calculate(rightPosition) + turn

    protected open fun drive(left: Double, right: Double) {
        Drivetrain.drive(left, right)
    }

    private companion object {
        const val TURN_IMPORTANCE = 0.0005

        val pathIndexField: Field = EncoderFollower::class.java.getDeclaredField("segment").apply {
            isAccessible = true
        }
    }
}

private abstract class ReversePathFollowerBase(path: PathBase) : PathFollowerBase(path) {
    override fun drive(left: Double, right: Double) = super.drive(-right, -left)

    override fun calculate(
            leftPosition: Int,
            rightPosition: Int,
            turn: Double
    ) = super.calculate(-rightPosition, -leftPosition, -turn)
}

private class LeftToLeftSwitch : PathFollowerBase(LeftToLeftSwitchPath)

private class RightToRightSwitch : PathFollowerBase(RightToRightSwitchPath)

private class MiddleToLeftSwitch : PathFollowerBase(MiddleToLeftSwitchPath)

private class MiddleToRightSwitch : PathFollowerBase(MiddleToRightSwitchPath)

private class LeftToLeftScale : PathFollowerBase(LeftToLeftScalePath)

private class RightToRightScale : PathFollowerBase(RightToRightScalePath)

private class LeftToRightScale : PathFollowerBase(LeftToRightScalePath)

private class RightToLeftScale : PathFollowerBase(RightToLeftScalePath)

private class LeftSwitchToRear : ReversePathFollowerBase(LeftSwitchToRearPath)

private class RightSwitchToRear : ReversePathFollowerBase(RightSwitchToRearPath)

private class TestLeft : PathFollowerBase(TestLeftPath)

private class TestRight : PathFollowerBase(TestRightPath)
