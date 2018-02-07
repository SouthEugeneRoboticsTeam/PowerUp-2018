package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sertain.util.SendableChooser

// Driver joysticks. Left and right used for tank drive, only right used for arcade drive.
val leftJoystick = Joystick(LEFT_STICK_PORT)
val rightJoystick = Joystick(RIGHT_STICK_PORT)

// Driver controller. Used in "controller" control mode.
val controller = XboxController(CONTROLLER_PORT)

// Gunner joystick.
val secondaryJoystick = Joystick(SECONDARY_STICK_PORT)

val controlMode
    get() = Preferences.getInstance().getString("control_mode", null).let {
        when (it) {
            "arcade" -> Control.Arcade
            "controller" -> Control.Controller
            else -> Control.Tank
        }
    }

val autoMode
    get() = Preferences.getInstance().getInt("auto_mode", 4).let {
        when (it) {
            1 -> AutoMode.LeftToLeft
            2 -> AutoMode.RightToRight
            3 -> AutoMode.MiddleToLeft
            4 -> AutoMode.MiddleToRight
            else -> AutoMode.CrossBaseline
        }
    }

val intakeSpeedScalar get() = Preferences.getInstance().getDouble("intake_speed_scalar", 0.5)

enum class Control {
    Tank, Arcade, Controller
}

enum class AutoMode {
    CrossBaseline, LeftToLeft, RightToRight, MiddleToLeft, MiddleToRight
}

object Dashboard {
    val autoMode = SendableChooser(
            "Cross Baseline" to AutoMode.CrossBaseline,
            "Left To Left" to AutoMode.LeftToLeft,
            "Middle To Left" to AutoMode.MiddleToLeft,
            "Middle To Right" to AutoMode.MiddleToRight,
            "Right To Right" to AutoMode.RightToRight
    )

    fun init() {
        SmartDashboard.putData("Auto Mode", autoMode)
    }
}
