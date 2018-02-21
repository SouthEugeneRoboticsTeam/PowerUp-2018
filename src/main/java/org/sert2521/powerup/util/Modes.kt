package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import openrio.powerup.MatchData.GameFeature
import openrio.powerup.MatchData.OwnedSide
import openrio.powerup.MatchData.getOwnedSide
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode
    get() {
        val startChoice = Modes.autoModeChooserStart.selected
        val endChoice = Modes.autoModeChooserEnd.selected
        val switchSide = getOwnedSide(GameFeature.SWITCH_NEAR)
        val scaleSide = getOwnedSide(GameFeature.SCALE)

        if (switchSide == OwnedSide.UNKNOWN || scaleSide == OwnedSide.UNKNOWN) {
            return Modes.autoModeChooser.selected
        }

        if (endChoice == AutoMode.End.BASELINE) {
            return AutoMode.CROSS_BASELINE
        }

        return when (switchSide) {
            OwnedSide.LEFT -> when (startChoice) {
                AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_LEFT_SWITCH
                AutoMode.Start.LEFT -> when (scaleSide) {
                    OwnedSide.LEFT -> when (endChoice) {
                        AutoMode.End.SWITCH -> AutoMode.LEFT_TO_LEFT_SWITCH
                        AutoMode.End.SCALE -> AutoMode.LEFT_TO_LEFT_SCALE_SWITCH
                        else -> error("Unknown mode: $endChoice")
                    }
                    OwnedSide.RIGHT -> AutoMode.RIGHT_TO_RIGHT_SWITCH
                    else -> error("Impossible condition: $scaleSide")
                }
                AutoMode.Start.RIGHT -> when (scaleSide) {
                    OwnedSide.LEFT -> AutoMode.CROSS_BASELINE
                    OwnedSide.RIGHT -> AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP
                    else -> error("Impossible condition: $scaleSide")
                }
                else -> AutoMode.CROSS_BASELINE // Should be impossible
            }
            OwnedSide.RIGHT -> when (startChoice) {
                AutoMode.Start.MIDDLE -> AutoMode.MIDDLE_TO_RIGHT_SWITCH
                AutoMode.Start.LEFT -> when (scaleSide) {
                    OwnedSide.LEFT -> AutoMode.LEFT_TO_LEFT_SCALE_PICKUP
                    OwnedSide.RIGHT -> AutoMode.CROSS_BASELINE
                    else -> error("Impossible condition: $scaleSide")
                }
                AutoMode.Start.RIGHT -> when (scaleSide) {
                    OwnedSide.LEFT -> AutoMode.RIGHT_TO_RIGHT_SWITCH
                    OwnedSide.RIGHT -> when (endChoice) {
                        AutoMode.End.SWITCH -> AutoMode.RIGHT_TO_RIGHT_SWITCH
                        AutoMode.End.SCALE -> AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH
                        else -> error("Unknown mode: $endChoice")
                    }
                    else -> error("Impossible condition: $scaleSide")
                }
                else -> AutoMode.CROSS_BASELINE // Should be impossible
            }
            else -> error("Impossible condition: $switchSide")
        }
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
    LEFT_TO_LEFT_SCALE_PICKUP, LEFT_TO_LEFT_SCALE_SWITCH,
    RIGHT_TO_RIGHT_SCALE_PICKUP, RIGHT_TO_RIGHT_SCALE_SWITCH,
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
            "Left to Left Scale with Pickup" to AutoMode.LEFT_TO_LEFT_SCALE_PICKUP,
            "Right to Right Scale with Pickup" to AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP,
            "Left to Left Scale with Switch" to AutoMode.LEFT_TO_LEFT_SCALE_SWITCH,
            "Right to Right Scale with Switch" to AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH,

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
