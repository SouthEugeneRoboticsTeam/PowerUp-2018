package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.XboxController

val leftJoystick = Joystick(LEFT_STICK_PORT)
val rightJoystick = Joystick(RIGHT_STICK_PORT)
val secondaryJoystick = Joystick(SECONDARY_STICK_PORT)

val controller = XboxController(CONTROLLER_PORT)

var controlMode
    get() = Preferences.getInstance().getString("control_mode", null).let {
        when (it) {
            "arcade" -> Control.Arcade
            "controller" -> Control.Controller
            else -> Control.Tank
        }
    }
    set(value) = Unit // Needed so Kotlin doesn't create a field

enum class Control {
    Tank, Arcade, Controller
}
