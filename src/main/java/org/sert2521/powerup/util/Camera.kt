package org.sert2521.powerup.util

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.DriverStation
import org.opencv.core.Core.FONT_HERSHEY_COMPLEX
import org.opencv.core.Core.FONT_HERSHEY_SIMPLEX
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoWriter
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.Executors

object Camera {
    // TODO: RoboRIO has very limited storage, so save to /U or /media/sda1 if a USB stick is plugged in
    private val ROOT = File("/home/lvuser/videos")
    private val executor = Executors.newSingleThreadExecutor()

    init {
        executor.execute {
            val ds = DriverStation.getInstance()
            val file = File(ROOT, "${ds.eventName}_${ds.matchType}_${ds.matchNumber}_${LocalDateTime.now()}.mp4")

            val camera = CameraServer.getInstance().startAutomaticCapture()
            camera.setResolution(1280, 720)

            val cvSink = CameraServer.getInstance().video
            val outputStream = CameraServer.getInstance().putVideo("Front Robot Camera", 1280, 720)

            val source = Mat()
            val out = VideoWriter(file.path, VideoWriter.fourcc('M', 'J', 'P', 'G'), 20.0, Size(640.0, 480.0))

            if (ROOT.exists() || ROOT.mkdirs()) {
                while (!Thread.interrupted()) {
                    cvSink.grabFrame(source)

                    val gameMode = when {
                        DriverStation.getInstance().isAutonomous -> "Autonomous"
                        DriverStation.getInstance().isDisabled -> "Disabled"
                        else -> "Teleoperated"
                    }

                    val allianceColor = when(DriverStation.getInstance().alliance) {
                        DriverStation.Alliance.Red -> Scalar(0.0, 0.0, 255.0)
                        DriverStation.Alliance.Blue -> Scalar(255.0, 0.0, 0.0)
                        else -> Scalar(255.0, 255.0, 255.0)
                    }

                    val text = "Time: ${DriverStation.getInstance().matchTime}\nMode: $gameMode"
                    Imgproc.putText(source, text, Point(0.0, 0.0), FONT_HERSHEY_SIMPLEX, 10.0, Scalar(0.0, 255.0, 0.0), 2)
                    Imgproc.rectangle(source, Point(5.0, 5.0), Point(165.0, 60.0), allianceColor, -1)
                    Imgproc.putText(
                            source,
                            DriverStation.getInstance().alliance.toString(),
                            Point(5.0, 53.0),
                            FONT_HERSHEY_COMPLEX, 1.85, Scalar(255.0, 255.0, 255.0),
                            2, 16, false
                    )

                    out.write(source)
                    outputStream.putFrame(source)
                }
            }
        }
    }
}
