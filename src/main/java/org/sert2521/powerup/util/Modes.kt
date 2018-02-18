package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import openrio.powerup.MatchData
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode
    get() = when (MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR)) {
        MatchData.OwnedSide.LEFT -> when (Modes.autoModeChooserStart.selected) {
            AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_LEFT_SWITCH
            AutoMode.Start.LEFT -> when (Modes.autoModeChooserEnd.selected) {
                AutoMode.End.BASELINE -> AutoMode.CROSS_BASELINE
                AutoMode.End.SWITCH -> AutoMode.LEFT_TO_LEFT_SWITCH
                AutoMode.End.SCALE -> AutoMode.LEFT_TO_LEFT_SCALE
                else -> error("Unknown mode: ${Modes.autoModeChooserEnd.selected}")
            }
            else -> AutoMode.CROSS_BASELINE
        }
        MatchData.OwnedSide.RIGHT -> when (Modes.autoModeChooserStart.selected) {
            AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_RIGHT_SWITCH
            AutoMode.Start.RIGHT -> when (Modes.autoModeChooserEnd.selected) {
                AutoMode.End.BASELINE -> AutoMode.CROSS_BASELINE
                AutoMode.End.SWITCH -> AutoMode.RIGHT_TO_RIGHT_SWITCH
                AutoMode.End.SCALE -> AutoMode.RIGHT_TO_RIGHT_SCALE
                else -> error("Unknown mode: ${Modes.autoModeChooserEnd.selected}")
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
    LEFT_TO_LEFT_SWITCH, RIGHT_TO_RIGHT_SWITCH,
    MIDDLE_TO_LEFT_SWITCH, MIDDLE_TO_RIGHT_SWITCH,
    LEFT_TO_LEFT_SCALE, RIGHT_TO_RIGHT_SCALE,
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
            "Controller" to Control.Controller(),
            "Arcade" to Control.Arcade(),
            "Tank" to Control.Tank(),
            "Curvature" to Control.Curvature()
    )
    val autoModeChooser = SendableChooser(
            "Cross Baseline" to AutoMode.CROSS_BASELINE,
            "Left to Left Switch" to AutoMode.LEFT_TO_LEFT_SWITCH,
            "Right to Right Switch" to AutoMode.RIGHT_TO_RIGHT_SWITCH,
            "Middle to Left Switch" to AutoMode.MIDDLE_TO_LEFT_SWITCH,
            "Middle to Right Switch" to AutoMode.MIDDLE_TO_RIGHT_SWITCH,
            "Left to Left Switch" to AutoMode.LEFT_TO_LEFT_SCALE,
            "Right to Left Switch" to AutoMode.RIGHT_TO_RIGHT_SCALE,

            "Test Left" to AutoMode.TEST_LEFT,
            "Test Right" to AutoMode.TEST_RIGHT
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
        SmartDashboard.putData("Control Mode", controlChooser)
        SmartDashboard.putData("Auto Mode", autoModeChooser)
        SmartDashboard.putData("Auto Mode Start Position", autoModeChooserStart)
        SmartDashboard.putData("Auto Mode End Result", autoModeChooserEnd)
    }
}
