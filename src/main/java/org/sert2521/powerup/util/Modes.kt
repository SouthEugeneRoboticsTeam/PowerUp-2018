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
            "CONTROLLER" to Control.Controller(),
            "ARCADE" to Control.Arcade(),
            "TANK" to Control.Tank(),
            "CURVATURE" to Control.Curvature()
    )
    val autoModeChooser = SendableChooser(
            "CROSS BASELINE" to AutoMode.CROSS_BASELINE,
            "LEFT TO LEFT SWITCH" to AutoMode.LEFT_TO_LEFT_SWITCH,
            "RIGHT TO RIGHT SWITCH" to AutoMode.RIGHT_TO_RIGHT_SWITCH,
            "MIDDLE TO LEFT SWITCH" to AutoMode.MIDDLE_TO_LEFT_SWITCH,
            "MIDDLE TO RIGHT SWITCH" to AutoMode.MIDDLE_TO_RIGHT_SWITCH,
            "LEFT TO LEFT SCALE" to AutoMode.LEFT_TO_LEFT_SCALE,
            "RIGHT TO LEFT SCALE" to AutoMode.RIGHT_TO_RIGHT_SCALE,

            "TEST LEFT" to AutoMode.TEST_LEFT,
            "TEST RIGHT" to AutoMode.TEST_RIGHT
    )
    val autoModeChooserStart = SendableChooser(
            "MIDDLE" to AutoMode.Start.MIDDLE,
            "LEFT" to AutoMode.Start.LEFT,
            "RIGHT" to AutoMode.Start.RIGHT
    )
    val autoModeChooserEnd = SendableChooser(
            "BASELINE" to AutoMode.End.BASELINE,
            "SWITCH" to AutoMode.End.SWITCH,
            "SCALE" to AutoMode.End.SCALE
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
