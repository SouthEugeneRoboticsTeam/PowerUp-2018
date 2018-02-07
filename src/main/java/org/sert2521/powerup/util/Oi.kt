package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.sert2521.powerup.autonomous.CrossBaselinePath
import org.sert2521.powerup.autonomous.LeftToLeftPath
import org.sert2521.powerup.autonomous.MiddleToRightPath
import org.sert2521.powerup.autonomous.RightToRightPath
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
            1 -> Auto.LeftToLeft
            2 -> Auto.RightToRight
            3 -> Auto.MiddleToLeft
            4 -> Auto.MiddleToRight
            else -> Auto.CrossBaseline
        }
    }

val intakeSpeedScalar get() = Preferences.getInstance().getDouble("intake_speed_scalar", 0.5)

enum class Control {
    Tank, Arcade, Controller
}

enum class Auto {
    CrossBaseline, LeftToLeft, RightToRight, MiddleToLeft, MiddleToRight
}

object Dashboard : SmartDashboard() {
    fun chooseAuto() {
        val autoChooser = SendableChooser("Cross Baseline" to CrossBaselinePath,
                                          "Left To Left" to LeftToLeftPath,
                                          "Middle To Left" to MiddleToRightPath,
                                          "Middle To Right" to MiddleToRightPath,
                                          "Right To Right" to RightToRightPath)
        SmartDashboard.putData(autoChooser)
    }
}
