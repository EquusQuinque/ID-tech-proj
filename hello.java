package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectionPipeline;

import java.util.ArrayList;

@Autonomous(name = "AprilTag Backward Auto")
public class AprilTagBackwardAuto extends LinearOpMode {

    DcMotor leftFront, rightFront, leftRear, rightRear;
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagPipeline;

    static final double COUNTS_PER_MOTOR_REV = 537.7; // GoBilda 312 RPM motor
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (Math.PI * WHEEL_DIAMETER_INCHES);

    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware mapping
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear   = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear  = hardwareMap.get(DcMotor.class, "rightRear");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);

        // Camera setup
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        aprilTagPipeline = new AprilTagDetectionPipeline(0.166, 578.272, 578.272, 402.145, 221.506);
        camera.setPipeline(aprilTagPipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // Check AprilTag detections
        ArrayList<AprilTagDetection> currentDetections = aprilTagPipeline.getLatestDetections();

        if (currentDetections != null) {
            for (AprilTagDetection tag : currentDetections) {
                telemetry.addData("Detected tag ID", tag.id);
                if (tag.id == 23) {
                    telemetry.addLine("Tag 23 found! Moving backward 10 inches.");
                    telemetry.update();
                    driveBackward(10, 0.5);
                }
            }
        }

        telemetry.addLine("Autonomous complete.");
        telemetry.update();
        sleep(2000);
    }

    public void driveBackward(double inches, double power) {
        int moveCounts = (int) (inches * COUNTS_PER_INCH);

        leftFront.setTargetPosition(leftFront.getCurrentPosition() - moveCounts);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() - moveCounts);
        leftRear.setTargetPosition(leftRear.getCurrentPosition() - moveCounts);
        rightRear.setTargetPosition(rightRear.getCurrentPosition() - moveCounts);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        rightFront.setPower(power);
        leftRear.setPower(power);
        rightRear.setPower(power);

        while (opModeIsActive() && leftFront.isBusy() && rightFront.isBusy()) {
            telemetry.addData("Driving", "Backward...");
            telemetry.update();
        }

        stopMotors();
    }

    private void stopMotors() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }
}

