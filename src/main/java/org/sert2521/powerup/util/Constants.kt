package org.sert2521.powerup.util

// Joysticks
const val LEFT_STICK_PORT = 0
const val RIGHT_STICK_PORT = 1

const val SECONDARY_STICK_PORT = 2

const val CONTROLLER_PORT = 3

// Talon IDs
const val LEFT_FRONT_MOTOR = 19
const val LEFT_REAR_MOTOR = 17
const val RIGHT_FRONT_MOTOR = 10
const val RIGHT_REAR_MOTOR = 14

const val LEFT_INTAKE_MOTOR = 12
const val RIGHT_INTAKE_MOTOR = 13

const val LEFT_ELEVATOR_MOTOR = -1
const val RIGHT_ELEVATOR_MOTOR = -1

const val LEFT_CLIMBER_MOTOR = -1
const val RIGHT_CLIMBER_MOTOR = -1

// Camera
const val CAMERA_FOV = 60 // Microsoft LifeCam HD 3000 FOV
const val CAMERA_WIDTH = 680
const val DEGREES_PER_PIXEL = CAMERA_FOV / CAMERA_WIDTH
