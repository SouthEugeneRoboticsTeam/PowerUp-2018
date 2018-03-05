package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import openrio.powerup.MatchData
import openrio.powerup.MatchData.GameFeature
import openrio.powerup.MatchData.OwnedSide
import openrio.powerup.MatchData.getOwnedSide
import org.sert2521.powerup.util.AutoMode.CROSS_BASELINE
import org.sert2521.powerup.util.AutoMode.Constraints
import org.sert2521.powerup.util.AutoMode.End
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SWITCH
import org.sert2521.powerup.util.AutoMode.LEFT_TO_LEFT_SWITCH_TWO_CUBE
import org.sert2521.powerup.util.AutoMode.LEFT_TO_RIGHT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.LEFT_TO_RIGHT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.MIDDLE_TO_LEFT_SWITCH
import org.sert2521.powerup.util.AutoMode.MIDDLE_TO_RIGHT_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_LEFT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_LEFT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SCALE_PICKUP
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SCALE_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SWITCH
import org.sert2521.powerup.util.AutoMode.RIGHT_TO_RIGHT_SWITCH_TWO_CUBE
import org.sert2521.powerup.util.AutoMode.Start
import org.sert2521.powerup.util.AutoMode.TEST_LEFT
import org.sert2521.powerup.util.AutoMode.TEST_RIGHT
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser
import java.io.File
import java.time.LocalDateTime

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode
    get() = calculateAutoMode(getOwnedSide(GameFeature.SWITCH_NEAR),
                              getOwnedSide(GameFeature.SCALE))

fun calculateAutoMode(switchSide: OwnedSide, scaleSide: OwnedSide): AutoMode {
    val startChoice: AutoMode.Start = Modes.autoStartChooser.selected
    val priorityChoice: AutoMode.End = Modes.autoPriorityChooser.selected
    val constraintsChoice: AutoMode.Constraints = Modes.autoConstraintsChooser.selected

    // If no game data is specified, use the chosen path
    if (switchSide == OwnedSide.UNKNOWN || scaleSide == OwnedSide.UNKNOWN) {
        return Modes.autoModeChooser.selected
    }

    // Don't worry about any logic if we're crossing the baseline
    if (priorityChoice == End.BASELINE) {
        return CROSS_BASELINE
    }

    return when (switchSide) {
        OwnedSide.LEFT -> when (startChoice) {
            Start.MIDDLE -> MIDDLE_TO_LEFT_SWITCH
            Start.LEFT -> when (scaleSide) {
                OwnedSide.LEFT -> when (priorityChoice) {
                    End.SWITCH -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        LEFT_TO_LEFT_SWITCH
                    } else {
                        LEFT_TO_LEFT_SWITCH_TWO_CUBE
                    }
                    End.SCALE -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        LEFT_TO_LEFT_SWITCH
                    } else {
                        LEFT_TO_LEFT_SCALE_SWITCH
                    }
                    else -> error("Unknown mode: $priorityChoice")
                }
                OwnedSide.RIGHT -> when (priorityChoice) {
                    End.SWITCH -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        LEFT_TO_LEFT_SWITCH
                    } else {
                        LEFT_TO_LEFT_SWITCH_TWO_CUBE
                    }
                    End.SCALE -> when (constraintsChoice) {
                        Constraints.NO_CROSS -> LEFT_TO_LEFT_SWITCH_TWO_CUBE
                        Constraints.NO_FAR_LANE -> LEFT_TO_LEFT_SWITCH
                        else -> LEFT_TO_RIGHT_SCALE_PICKUP
                    }
                    else -> error("Unknown mode: $priorityChoice")
                }
                else -> error("Impossible condition: $scaleSide")
            }
            Start.RIGHT -> when (scaleSide) {
                OwnedSide.LEFT -> if (constraintsChoice == Constraints.NO_FAR_LANE || constraintsChoice == Constraints.NO_CROSS) {
                    CROSS_BASELINE
                } else {
                    RIGHT_TO_LEFT_SCALE_SWITCH
                }
                OwnedSide.RIGHT -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                    CROSS_BASELINE
                } else {
                    RIGHT_TO_RIGHT_SCALE_PICKUP
                }
                else -> error("Impossible condition: $scaleSide")
            }
        }
        OwnedSide.RIGHT -> when (startChoice) {
            Start.MIDDLE -> MIDDLE_TO_RIGHT_SWITCH
            Start.LEFT -> when (scaleSide) {
                OwnedSide.LEFT -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                    CROSS_BASELINE
                } else {
                    LEFT_TO_LEFT_SCALE_PICKUP
                }
                OwnedSide.RIGHT -> if (constraintsChoice == Constraints.NO_FAR_LANE || constraintsChoice == Constraints.NO_CROSS) {
                    CROSS_BASELINE
                } else {
                    LEFT_TO_RIGHT_SCALE_SWITCH
                }
                else -> error("Impossible condition: $scaleSide")
            }
            Start.RIGHT -> when (scaleSide) {
                OwnedSide.LEFT -> when (priorityChoice) {
                    End.SWITCH -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        RIGHT_TO_RIGHT_SWITCH
                    } else {
                        RIGHT_TO_RIGHT_SWITCH_TWO_CUBE
                    }
                    End.SCALE -> when (constraintsChoice) {
                        Constraints.NO_CROSS -> RIGHT_TO_RIGHT_SWITCH_TWO_CUBE
                        Constraints.NO_FAR_LANE -> RIGHT_TO_RIGHT_SWITCH
                        else -> RIGHT_TO_LEFT_SCALE_PICKUP
                    }
                    else -> error("Unknown mode: $priorityChoice")
                }
                OwnedSide.RIGHT -> when (priorityChoice) {
                    End.SWITCH -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        RIGHT_TO_RIGHT_SWITCH
                    } else {
                        RIGHT_TO_RIGHT_SWITCH_TWO_CUBE
                    }
                    End.SCALE -> if (constraintsChoice == Constraints.NO_FAR_LANE) {
                        RIGHT_TO_RIGHT_SWITCH
                    } else {
                        RIGHT_TO_RIGHT_SCALE_SWITCH
                    }
                    else -> error("Unknown mode: $priorityChoice")
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
    LEFT_TO_LEFT_SWITCH_TWO_CUBE, RIGHT_TO_RIGHT_SWITCH_TWO_CUBE,
    LEFT_TO_LEFT_SCALE_PICKUP, LEFT_TO_LEFT_SCALE_SWITCH,
    LEFT_TO_RIGHT_SCALE_PICKUP, LEFT_TO_RIGHT_SCALE_SWITCH,
    RIGHT_TO_RIGHT_SCALE_PICKUP, RIGHT_TO_RIGHT_SCALE_SWITCH,
    RIGHT_TO_LEFT_SCALE_PICKUP, RIGHT_TO_LEFT_SCALE_SWITCH,

    TEST_LEFT, TEST_RIGHT;

    enum class Start {
        LEFT, MIDDLE, RIGHT
    }

    enum class End {
        BASELINE, SWITCH, SCALE
    }

    enum class Constraints {
        NONE, NO_CROSS, NO_FAR_LANE
    }
}

object Modes : RobotLifecycle {
    private val ROOT = File("/home/lvuser/auto")

    val controlChooser = SendableChooser(
            "Controller" to Control.Controller(),
            "Arcade" to Control.Arcade(),
            "Tank" to Control.Tank(),
            "Curvature" to Control.Curvature()
    )
    val autoModeChooser = SendableChooser(
            "Cross Baseline" to CROSS_BASELINE,
            "Left to Left Switch" to LEFT_TO_LEFT_SWITCH,
            "Left to Left Switch Two Cube" to LEFT_TO_LEFT_SWITCH_TWO_CUBE,
            "Right to Right Switch" to RIGHT_TO_RIGHT_SWITCH,
            "Middle to Left Switch" to MIDDLE_TO_LEFT_SWITCH,
            "Middle to Right Switch" to MIDDLE_TO_RIGHT_SWITCH,
            "Left to Left Scale with Pickup" to LEFT_TO_LEFT_SCALE_PICKUP,
            "Right to Right Scale with Pickup" to RIGHT_TO_RIGHT_SCALE_PICKUP,
            "Left to Left Scale with Switch" to LEFT_TO_LEFT_SCALE_SWITCH,
            "Right to Right Scale with Switch" to RIGHT_TO_RIGHT_SCALE_SWITCH,
            "Left to Right Scale with Pickup" to LEFT_TO_RIGHT_SCALE_PICKUP,
            "Right to Left Scale with Pickup" to RIGHT_TO_LEFT_SCALE_PICKUP,
            "Left to Right Scale with Switch" to LEFT_TO_RIGHT_SCALE_SWITCH,
            "Right to Left Scale with Switch" to RIGHT_TO_LEFT_SCALE_SWITCH,

            "Test Left" to TEST_LEFT,
            "Test Right" to TEST_RIGHT
    )
    val autoStartChooser = SendableChooser(
            "Middle" to Start.MIDDLE,
            "Left" to Start.LEFT,
            "Right" to Start.RIGHT
    )
    val autoPriorityChooser = SendableChooser(
            "Switch" to End.SWITCH,
            "Scale" to End.SCALE,
            "Baseline" to End.BASELINE
    )
    val autoConstraintsChooser = SendableChooser(
            "None" to Constraints.NONE,
            "No Cross" to Constraints.NO_CROSS,
            "No Far Lane" to Constraints.NO_FAR_LANE
    )

    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        SmartDashboard.putData("Control Mode", controlChooser)
        SmartDashboard.putData("Auto Mode", autoModeChooser)
        SmartDashboard.putData("Auto Start Position", autoStartChooser)
        SmartDashboard.putData("Auto Objective", autoPriorityChooser)
        SmartDashboard.putData("Auto Constraints", autoConstraintsChooser)
    }

    override fun executeDisabled() {
        if (autoModeChooser.selected != autoModeChooser ||
                autoStartChooser.selected != autoStartChooser ||
                autoPriorityChooser.selected != autoPriorityChooser) updateModeFeedback()
    }

    override fun onAutoStart() {
        val ds = DriverStation.getInstance()
        val file = File(ROOT, "${ds.matchType}_${ds.matchNumber}_${LocalDateTime.now()}.txt")

        val startChoice: AutoMode.Start = Modes.autoStartChooser.selected
        val priorityChoice: AutoMode.End = Modes.autoPriorityChooser.selected
        val constraintsChoice: AutoMode.Constraints = Modes.autoConstraintsChooser.selected

        file.writeText("""
            Options
            ------------
            START POSITION: ${startChoice.name}
            PRIORITY: ${priorityChoice.name}
            CONSTRAINTS: ${constraintsChoice.name}

            Modes (based on options)
            ------------
            LLL: ${calculateAutoMode(MatchData.OwnedSide.LEFT, MatchData.OwnedSide.LEFT).name}
            RRR: ${calculateAutoMode(MatchData.OwnedSide.RIGHT, MatchData.OwnedSide.RIGHT).name}
            LRL: ${calculateAutoMode(MatchData.OwnedSide.LEFT, MatchData.OwnedSide.RIGHT).name}
            RLR: ${calculateAutoMode(MatchData.OwnedSide.RIGHT, MatchData.OwnedSide.LEFT).name}

            Match
            ------------
            FIELD CODE: ${ds.gameSpecificMessage}
            ACTUAL MODE: ${autoMode.name}
            """.trimIndent())
    }

    private fun updateModeFeedback() {
        SmartDashboard.putString("LLL Mode",
                                 calculateAutoMode(MatchData.OwnedSide.LEFT,
                                                   MatchData.OwnedSide.LEFT).name)
        SmartDashboard.putString("RRR Mode",
                                 calculateAutoMode(MatchData.OwnedSide.RIGHT,
                                                   MatchData.OwnedSide.RIGHT).name)
        SmartDashboard.putString("LRL Mode",
                                 calculateAutoMode(MatchData.OwnedSide.LEFT,
                                                   MatchData.OwnedSide.RIGHT).name)
        SmartDashboard.putString("RLR Mode",
                                 calculateAutoMode(MatchData.OwnedSide.RIGHT,
                                                   MatchData.OwnedSide.LEFT).name)
    }
}
