package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutonLeft extends LinearOpMode {

    private DcMotor linearSlideMotor;
    private Mecanum mecanum;
    private Servo claw;

    //private ComputerVision cv;
    private int recognition;

    @Override
    public void runOpMode() throws InterruptedException {

        mecanum = new Mecanum(
                hardwareMap.get(BNO055IMU.class, "imu"),
                hardwareMap.get(DcMotor.class, "frontLeft"),
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "backRight"),
                hardwareMap.get(DcMotor.class, "backLeft")
        );
        mecanum.constantSpeed();

        linearSlideMotor = hardwareMap.get(DcMotor.class, "linearSlide");
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        claw = hardwareMap.get(Servo.class, "clawServo");
        claw.setPosition(0.3);   // closed claw

        // cv = new ComputerVision(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        waitForStart();

        if (opModeIsActive()) {

            //recognition = 1; // cv.getRecognition();



            mecanum.forward(0.2, 0, 1200);




            // power, angle, duration
            mecanum.forward(0.5, 0, 2000);

            // power, targetAngle
            mecanum.yaw(-0.5, 90);

            // power, angle, duration
            mecanum.drift(0.5, 90, 2000);


//            if (recognition == 1) {
//
//            } else if (recognition == 2) {
//
//            } else {
//
//            }

        }
    }
}