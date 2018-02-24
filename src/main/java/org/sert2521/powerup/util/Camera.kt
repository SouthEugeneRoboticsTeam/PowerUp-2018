package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.DriverStation
import org.opencv.core.Core.FONT_HERSHEY_SIMPLEX
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoWriter
import java.io.File
import java.time.LocalDateTime
import kotlin.concurrent.thread

object Camera {
    // TODO: RoboRIO has very limited storage, so save to /U or /media/sda1 if a USB stick is plugged in
    private val ROOT = File("/home/lvuser/videos")

    init {
        thread {
            val ds = DriverStation.getInstance()
            val file = File(ROOT, "${ds.eventName}_${ds.matchType}_${ds.matchNumber}_${LocalDateTime.now()}.mp4")

            val camera = CameraServer.getInstance().startAutomaticCapture()
            camera.setResolution(1280, 720)
            camera.setExposureManual(50)
            camera.setWhiteBalanceManual(75)

            val cvSink = CameraServer.getInstance().video
            val outputStream = CameraServer.getInstance().putVideo("Front Robot Camera", 1280, 720)

            val source = Mat()
            val out = VideoWriter(file.path, VideoWriter.fourcc('M', 'J', 'P', 'G'), 20.0, Size(640.0, 480.0))

            println("Writing...")

            if (ROOT.exists() || ROOT.mkdirs()) {
                println("Starting...")
                var running = true
                var turn = 0
                while (running) {
                    cvSink.grabFrame(source)

                    val gameMode = when {
                        DriverStation.getInstance().isAutonomous -> "Autonomous"
                        DriverStation.getInstance().isDisabled -> "Disabled"
                        else -> "Teleoperated"
                    }

                    val text = "Time: ${DriverStation.getInstance().matchTime}, Mode: $gameMode, Game Data: ${DriverStation.getInstance().gameSpecificMessage}"
                    Imgproc.putText(source, text, Point(20.0, 40.0), FONT_HERSHEY_SIMPLEX, 1.0, Scalar(0.0, 255.0, 0.0), 2)

                    out.write(source)
                    if (turn != 0) Imgcodecs.imwrite(File(ROOT, "$turn.jpg").path, source)
                    println(turn)

                    turn++

                    if (turn == 51) {
                        running = false
                        out.release()
                    }
                }
            }
        }
    }
}
