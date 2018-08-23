package org.sert2521.powerup.util

// Joysticks
const val LEFT_STICK_PORT = 0
const val RIGHT_STICK_PORT = 1

const val SECONDARY_STICK_PORT = 2

const val CONTROLLER_PORT = 3

// Talon IDs
const val LEFT_FRONT_MOTOR = 10
const val LEFT_REAR_MOTOR = 11
const val RIGHT_FRONT_MOTOR = 12
const val RIGHT_REAR_MOTOR = 13

const val LEFT_INTAKE_MOTOR = 0
const val RIGHT_INTAKE_MOTOR = 1

const val LEFT_ELEVATOR_MOTOR = 14
const val RIGHT_ELEVATOR_MOTOR = 15

const val FRONT_CLIMBER_MOTOR = 16
const val REAR_CLIMBER_MOTOR = 17

// Sensors
const val BOTTOM_TRIGGER_PORT = 1
const val MIDDLE_TRIGGER_PORT = 4
const val TOP_TRIGGER_PORT = 0
const val SWITCH_TRIGGER_PORT = 5
const val INTAKE_TRIGGER_PORT = 2

// LEDs
const val RED_LED_PORT = 6
const val BLUE_LED_PORT = 7

// Auto
const val ENCODER_TICKS_PER_REVOLUTION = 8192
const val WHEEL_DIAMETER = 0.15
const val WHEELBASE_WIDTH = 0.7
const val MAX_VELOCITY = 0.6
const val MAX_ACCELERATION = 0.1
const val MAX_JERK = 60.0

// TimeSync
const val JETSON_PORT = 2521 // UDP Port for Jetson
const val JETSON_IP = "127.0.0.1" // Set to actual IP (or broadcast address) before deploying

// Other
const val DEGREES_PER_PIXEL = 53.4 / 680 // Logitech Webcam C270 FOV in degrees / pixel width
const val UDP_PORT = 5800
