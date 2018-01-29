package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController

val leftJoystick: Joystick = Joystick(LEFT_STICK_PORT)
val rightJoystick: Joystick = Joystick(RIGHT_STICK_PORT)

val secondaryJoystick: Joystick = Joystick(SECONDARY_STICK_PORT)

val controller: XboxController = XboxController(CONTROLLER_PORT)

enum class Control {
    Arcade, Tank, Controller
}

var controlMode = Control.Controller
