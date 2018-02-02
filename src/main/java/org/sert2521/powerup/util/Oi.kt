package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.XboxController

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

val intakeSpeedScalar get() = Preferences.getInstance().getDouble("intake_speed_scalar", 0.5)

enum class Control {
    Tank, Arcade, Controller
}
