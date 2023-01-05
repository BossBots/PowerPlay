package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutonRight extends LinearOpMode {

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
        linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        claw = hardwareMap.get(Servo.class, "clawServo");
        claw.setPosition(0.3);   // assuming 0.3 is an open claw

        // cv = new ComputerVision(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        waitForStart();
        recognition = 3 ;
        claw.setPosition(0.3);
        if (opModeIsActive()) {
            linearSlideMotor.setTargetPosition(-3300);
            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlideMotor.setPower(-0.3);
            mecanum.forward(0.2, 0, 1250);
            while (linearSlideMotor.isBusy()){
                idle();
            }
            linearSlideMotor.setPower(0);
            linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            mecanum.yaw(-0.25, 35);
            mecanum.forward(0.1, 0, 1500);
            claw.setPosition(0.1);
            mecanum.forward(-0.1, 180,800);
            mecanum.yaw(0.25, -86);
            if (recognition ==3){ //orange
                mecanum.forward(0.2, 0, 1200);
            } else if (recognition ==1){ //green
                mecanum.forward(-0.2, 0, -1200);
            } else if (recognition ==4){ //none
                mecanum.yaw(0.25, -90);
                mecanum.forward(0.2, 0, 1200);
                mecanum.yaw(-0.25, 90);
                mecanum.forward(0.2, 0, 1200);
            }



            /*
            // power, angle, duration
            mecanum.forward(0.5, 0, 2000);

            // power, targetAngle
            mecanum.yaw(-0.5, 90);

            // power, angle, duration
            mecanum.drift(0.5, 90, 2000);
            */

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