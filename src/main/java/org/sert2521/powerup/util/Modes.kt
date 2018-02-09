package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sertain.RobotLifecycle
import org.sertain.util.SendableChooser

val controlMode: Control get() = Modes.controlChooser.selected

val autoMode: AutoMode get() = Modes.autoModeChooser.selected

sealed class Control {
    class Tank : Control()

    open class Arcade : Control()
    class Curvature : Arcade()

    class Controller : Control()
}

enum class AutoMode {
    CrossBaseline, LeftToLeft, RightToRight, MiddleToLeft, MiddleToRight
}

object Modes : RobotLifecycle {
    val controlChooser = SendableChooser(
            "Arcade" to Control.Arcade(),
            "Tank" to Control.Tank(),
            "Curvature" to Control.Curvature(),
            "Controller" to Control.Controller()
    )
    val autoModeChooser = SendableChooser(
            "Cross Baseline" to AutoMode.CrossBaseline,
            "Left To Left" to AutoMode.LeftToLeft,
            "Middle To Left" to AutoMode.MiddleToLeft,
            "Middle To Right" to AutoMode.MiddleToRight,
            "Right To Right" to AutoMode.RightToRight
    )

    init {
        RobotLifecycle.addListener(this)
    }

    override fun onCreate() {
        SmartDashboard.putData("Control mode", controlChooser)
        SmartDashboard.putData("Auto Mode", autoModeChooser)
    }
}
