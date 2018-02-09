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

// Auto
const val ENCODER_TICKS_PER_REVOLUTION = 8192
const val WHEEL_DIAMETER = 0.15
const val WHEELBASE_WIDTH = 0.7
const val MAX_VELOCITY = 0.75
const val MAX_ACCELERATION = 0.075
const val MAX_JERK = 60.0

// Camera
const val CAMERA_FOV = 60 // Microsoft LifeCam HD 3000 field of view
const val CAMERA_WIDTH = 680
const val DEGREES_PER_PIXEL = CAMERA_FOV / CAMERA_WIDTH
