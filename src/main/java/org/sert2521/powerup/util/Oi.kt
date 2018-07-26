package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

// Driver joysticks. Left and right used for tank drive, only right used for arcade drive.
val leftJoystick by lazy { Joystick(LEFT_STICK_PORT) }
val rightJoystick by lazy { Joystick(RIGHT_STICK_PORT) }

// Driver controller. Used in "controller" control mode.
val controller by lazy { XboxController(CONTROLLER_PORT) }

// Gunner joystick.
val secondaryJoystick by lazy { Joystick(SECONDARY_STICK_PORT) }

val intakeSpeedScalar get() = Preferences.getInstance().getDouble("intake_speed_scalar", 0.8)
val normalEjectSpeedScalar
    get() = Preferences.getInstance().getDouble("normal_eject_speed_scalar", 0.6)
val fastEjectSpeedScalar get() = Preferences.getInstance().getDouble("fast_eject_speed_scalar", 1.0)
val driveSpeedScalar get() = Preferences.getInstance().getDouble("drive_speed_scalar", 0.85)

fun initPreferences() {
    Preferences.getInstance().putDouble("intake_speed_scalar", intakeSpeedScalar)
    Preferences.getInstance().putDouble("normal_eject_speed_scalar", normalEjectSpeedScalar)
    Preferences.getInstance().putDouble("fast_eject_speed_scalar", fastEjectSpeedScalar)
    Preferences.getInstance().putDouble("drive_speed_scalar", driveSpeedScalar)
}

fun logTelemetry() {
    "branch.txt".asResource {
        println("Branch: $it")
        SmartDashboard.putString("branch", it)
    }

    "commit.txt".asResource {
        println("Commit: $it")
        SmartDashboard.putString("commit", it)
    }

    "changes.txt".asResource {
        println("Changes: $it")
        SmartDashboard.putString("changes", it)
    }

    "buildtime.txt".asResource {
        println("Buildtime: $it")
        SmartDashboard.putString("buildtime", it)
    }
}

fun String.asResource(work: (String) -> Unit) {
    val content = this.javaClass::class.java.getResource(this).readText()
    work(content)
}
