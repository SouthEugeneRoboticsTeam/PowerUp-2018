package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController

val leftJoystick = Joystick(LEFT_STICK_PORT)
val rightJoystick = Joystick(RIGHT_STICK_PORT)
val secondaryJoystick = Joystick(SECONDARY_STICK_PORT)

val controller = XboxController(CONTROLLER_PORT)

var controlMode = Control.Controller

enum class Control {
    Arcade, Tank, Controller
}
