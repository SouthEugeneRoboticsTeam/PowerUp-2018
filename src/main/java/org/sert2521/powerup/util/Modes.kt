package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import openrio.powerup.MatchData.GameFeature
import openrio.powerup.MatchData.OwnedSide
import openrio.powerup.MatchData.getOwnedSide
import org.sert2521.powerup.util.AutoMode.CROSS_BASELINE
import org.sert2521.powerup.util.AutoMode.End
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SWITCH
import org.sert2521.powerup.util.AutoMode.LEFT_TO_RIGHT_SCALE
import org.sert2521.powerup.util.AutoMode.MIDDLE_TO_LEFT_SWITCH
import org.sert2521.powerup.util.AutoMode.MIDDLE_TO_RIGHT_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_LEFT_SCALE
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SWITCH
import org.sert2521.powerup.util.AutoMode.Start
import org.sert2521.powerup.util.AutoMode.TEST_LEFT
import org.sert2521.powerup.util.AutoMode.TEST_RIGHT
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode
    get() {
        val startChoice: AutoMode.Start = Modes.autoModeChooserStart.selected
        val endChoice: AutoMode.End = Modes.autoModeChooserEnd.selected
        val switchSide: OwnedSide = getOwnedSide(GameFeature.SWITCH_NEAR)
        val scaleSide: OwnedSide = getOwnedSide(GameFeature.SCALE)

        if (switchSide == OwnedSide.UNKNOWN || scaleSide == OwnedSide.UNKNOWN) {
            return Modes.autoModeChooser.selected
        }

        if (endChoice == End.BASELINE) {
            return CROSS_BASELINE
        }

        return when (switchSide) {
            OwnedSide.LEFT -> when (startChoice) {
                Start.MIDDLE -> MIDDLE_TO_LEFT_SWITCH
                Start.LEFT -> when (scaleSide) {
                    OwnedSide.LEFT -> when (endChoice) {
                        End.SWITCH -> LEFT_TO_LEFT_SWITCH
                        End.SCALE -> LEFT_TO_LEFT_SCALE_SWITCH
                        else -> error("Unknown mode: $endChoice")
                    }
                    OwnedSide.RIGHT -> RIGHT_TO_RIGHT_SWITCH
                    else -> error("Impossible condition: $scaleSide")
                }
                Start.RIGHT -> when (scaleSide) {
                    OwnedSide.LEFT -> RIGHT_TO_LEFT_SCALE
                    OwnedSide.RIGHT -> RIGHT_TO_RIGHT_SCALE_PICKUP
                    else -> error("Impossible condition: $scaleSide")
                }
            }
            OwnedSide.RIGHT -> when (startChoice) {
                Start.MIDDLE -> MIDDLE_TO_RIGHT_SWITCH
                Start.LEFT -> when (scaleSide) {
                    OwnedSide.LEFT -> LEFT_TO_LEFT_SCALE_PICKUP
                    OwnedSide.RIGHT -> LEFT_TO_RIGHT_SCALE
                    else -> error("Impossible condition: $scaleSide")
                }
                Start.RIGHT -> when (scaleSide) {
                    OwnedSide.LEFT -> RIGHT_TO_RIGHT_SWITCH
                    OwnedSide.RIGHT -> when (endChoice) {
                        End.SWITCH -> RIGHT_TO_RIGHT_SWITCH
                        End.SCALE -> RIGHT_TO_RIGHT_SCALE_SWITCH
                        else -> error("Unknown mode: $endChoice")
                    }
                    else -> error("Impossible condition: $scaleSide")
                }
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
    LEFT_TO_LEFT_SCALE_PICKUP, LEFT_TO_LEFT_SCALE_SWITCH, LEFT_TO_RIGHT_SCALE,
    RIGHT_TO_RIGHT_SCALE_PICKUP, RIGHT_TO_RIGHT_SCALE_SWITCH, RIGHT_TO_LEFT_SCALE,
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
            "Cross Baseline" to CROSS_BASELINE,
            "Left to Left Switch" to LEFT_TO_LEFT_SWITCH,
            "Right to Right Switch" to RIGHT_TO_RIGHT_SWITCH,
            "Middle to Left Switch" to MIDDLE_TO_LEFT_SWITCH,
            "Middle to Right Switch" to MIDDLE_TO_RIGHT_SWITCH,
            "Left to Left Scale with Pickup" to LEFT_TO_LEFT_SCALE_PICKUP,
            "Right to Right Scale with Pickup" to RIGHT_TO_RIGHT_SCALE_PICKUP,
            "Left to Left Scale with Switch" to LEFT_TO_LEFT_SCALE_SWITCH,
            "Right to Right Scale with Switch" to RIGHT_TO_RIGHT_SCALE_SWITCH,
            "Left to Right Scale" to LEFT_TO_RIGHT_SCALE,
            "Right to Left Scale" to RIGHT_TO_LEFT_SCALE,

            "Test Left" to TEST_LEFT,
            "Test Right" to TEST_RIGHT
    )
    val autoModeChooserStart = SendableChooser(
            "Middle" to Start.MIDDLE,
            "Left" to Start.LEFT,
            "Right" to Start.RIGHT
    )
    val autoModeChooserEnd = SendableChooser(
            "Baseline" to End.BASELINE,
            "Switch" to End.SWITCH,
            "Scale" to End.SCALE
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
