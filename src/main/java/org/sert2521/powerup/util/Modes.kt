package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import openrio.powerup.MatchData
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode
    get() = when (MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR)) {
        MatchData.OwnedSide.LEFT -> when (Modes.autoModeChooserStart) {
            AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_LEFT
            AutoMode.Start.LEFT -> when (Modes.autoModeChooserEnd) {
                AutoMode.End.BASELINE -> AutoMode.CROSS_BASELINE
                AutoMode.End.SWITCH -> AutoMode.LEFT_TO_LEFT
                AutoMode.End.SCALE -> AutoMode.LEFT_TO_SCALE
                else -> error("Unknown mode: ${Modes.autoModeChooserEnd}")
            }
            else -> AutoMode.CROSS_BASELINE
        }
        MatchData.OwnedSide.RIGHT -> when (Modes.autoModeChooserStart) {
            AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_RIGHT
            AutoMode.Start.RIGHT -> when (Modes.autoModeChooserEnd) {
                AutoMode.End.BASELINE -> AutoMode.CROSS_BASELINE
                AutoMode.End.SWITCH -> AutoMode.RIGHT_TO_RIGHT
                AutoMode.End.SCALE -> AutoMode.RIGHT_TO_SCALE
                else -> error("Unknown mode: ${Modes.autoModeChooserEnd}")
            }
            else -> AutoMode.CROSS_BASELINE
        }
        else -> Modes.autoModeChooser.selected
    }

sealed class Control {
    class Tank : Control()

    open class Arcade : Control()
    class Curvature : Arcade()

    class Controller : Control()
}

enum class AutoMode {
    CROSS_BASELINE,
    LEFT_TO_LEFT, LEFT_TO_SCALE,
    RIGHT_TO_RIGHT, RIGHT_TO_SCALE,
    MIDDLE_TO_LEFT, MIDDLE_TO_RIGHT,
    TEST_LEFT, TEST_RIGHT;

    enum class Start {
        LEFT, MIDDLE, RIGHT
    }

    enum class End {
        BASELINE, SWITCH, SCALE
    }
}

object Modes : RobotLifecycle {
    val controlChooser = SendableChooser(
            "Arcade" to Control.Arcade(),
            "Tank" to Control.Tank(),
            "Curvature" to Control.Curvature(),
            "Controller" to Control.Controller()
    )
    val autoModeChooser = SendableChooser(
            "Cross baseline" to AutoMode.CROSS_BASELINE,
            "Left to left switch" to AutoMode.LEFT_TO_LEFT,
            "Left to left scale" to AutoMode.LEFT_TO_SCALE,
            "Right to right switch" to AutoMode.RIGHT_TO_RIGHT,
            "Right to right scale" to AutoMode.RIGHT_TO_SCALE,
            "Middle to left switch" to AutoMode.MIDDLE_TO_LEFT,
            "Middle to right switch" to AutoMode.MIDDLE_TO_RIGHT,
            "Test left" to AutoMode.TEST_LEFT,
            "Test right" to AutoMode.TEST_RIGHT
    )
    val autoModeChooserStart = SendableChooser(
            "Middle" to AutoMode.Start.MIDDLE,
            "Left" to AutoMode.Start.LEFT,
            "Right" to AutoMode.Start.RIGHT
    )
    val autoModeChooserEnd = SendableChooser(
            "Baseline" to AutoMode.End.BASELINE,
            "Switch" to AutoMode.End.SWITCH,
            "Scale" to AutoMode.End.SCALE
    )

    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        SmartDashboard.putData("Control mode", controlChooser)
        SmartDashboard.putData("Auto mode", autoModeChooser)
        SmartDashboard.putData("Auto mode start position", autoModeChooserStart)
        SmartDashboard.putData("Auto mode end result", autoModeChooserEnd)
    }
}
