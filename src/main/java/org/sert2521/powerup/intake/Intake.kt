package org.sert2521.powerup.intake

<<<<<<< Updated upstream
import org.sert2521.powerup.intake.commands.TeleopIntake
import org.sert2521.powerup.util.LEFT_INTAKE_MOTOR
import org.sert2521.powerup.util.RIGHT_INTAKE_MOTOR
import org.sertain.RobotLifecycle
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.autoBreak
import org.sertain.hardware.plus

object Intake : Subsystem(), RobotLifecycle {
    private val intake =
            Talon(LEFT_INTAKE_MOTOR).autoBreak() + Talon(RIGHT_INTAKE_MOTOR).autoBreak()

    override val defaultCommand = TeleopIntake()

    override fun onStart() = stop()

    fun set(speed: Double) = intake.set(speed)

    fun stop() = intake.stopMotor()
=======
import org.sert2521.powerup.util.LEFT_INTAKE_MOTOR
import org.sert2521.powerup.util.RIGHT_INTAKE_MOTOR
import org.sert2521.powerup.util.leftJoystick
import org.sertain.command.Command
import org.sertain.command.Subsystem
import org.sertain.hardware.Talon
import org.sertain.hardware.plus
import org.sertain.hardware.whileActive

val intake: Talon = Talon(LEFT_INTAKE_MOTOR) + Talon(RIGHT_INTAKE_MOTOR)

class Intake: Subsystem() {
    override fun onCreate() {
        leftJoystick.whileActive(1, RunIntake(true, intake))
    }
>>>>>>> Stashed changes
}
