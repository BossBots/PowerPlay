package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import java.util.List;

@Autonomous
public class AutonLeft extends LinearOpMode {
    // mecanum drive train
    private Mecanum mecanum;
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private ComputerVision cv;
    private DcMotor linSlide;
    private DcMotor arm;
    private Servo claw;
    // wobble goal manipulation system
    private DcMotor pivot;
    private Servo lock;

    // ring manipulation system
    private DcMotor launch;
    private DcMotor ramp;
    private CRServo intake;
    private CRServo ramp_rings1;
    private CRServo ramp_rings2;

    // image recognition system
    //private ImageRecognition ir;
    private String numRings;


    @Override
    public void runOpMode() {

        // mecanum drive train
        mecanum = new Mecanum(hardwareMap.get(BNO055IMU.class, "imu"), hardwareMap.get(DcMotor.class, "motor1"), hardwareMap.get(DcMotor.class, "motor2"), hardwareMap.get(DcMotor.class, "motor3"), hardwareMap.get(DcMotor.class, "motor4"));
        mecanum.constantSpeed();
        linSlide = hardwareMap.get(DcMotor.class, "linSlide");
        linSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        claw = hardwareMap.get(Servo.class, "claw");
        cv = new ComputerVision(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {


            //sleep(1500);

            //mecanum.drift(-0.25, 0, 1000);


            while (opModeIsActive()) {

                double y = -gamepad1.left_stick_y; // Remember, this is reversed!
                double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
                double rx = gamepad1.right_stick_x;

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double frontLeftPower = (y + x + rx) / denominator;
                double backLeftPower = (y - x + rx) / denominator;
                double frontRightPower = (y - x - rx) / denominator;
                double backRightPower = (y + x - rx) / denominator;

                motor1.setPower(frontLeftPower);
                motor2.setPower(backLeftPower);
                motor3.setPower(frontRightPower);
                motor4.setPower(backRightPower);
            }


            //telemetry.addData("rings", numRings);
            telemetry.update();
//            sleep(1000);
//
//            // place the 1st wobble goal
//            mecanum.drift(0.5, 0, 500);
//            if (numRings.equals("Quad")) {
//                mecanum.forward(0.6, 0, 2000);
//                mecanum.drift(-0.5, 0, 750);
//                while (pivot.getCurrentPosition() < 150) {
//                    pivot.setPower(0.25);
//                }
//                pivot.setPower(0);
//                lock.setPosition(1);
//                mecanum.drift(0.5, 0, 750);
//                mecanum.forward(-0.5, 0, 1250);
//            } else if (numRings.equals("Single")) {
//                mecanum.forward(0.6, 0, 1750);
//                //mecanum.drift(-0.5, 0, 350);
//                while (pivot.getCurrentPosition() < 150) {
//                    pivot.setPower(0.25);
//                }
//                pivot.setPower(0);
//                lock.setPosition(1);
//                mecanum.drift(0.5, 0, 250);
//                mecanum.forward(-0.5, 0, 750);
//            } else {
//                mecanum.forward(0.6, 0, 1250);
//                mecanum.drift(-0.5, 0, 750);
//                while (pivot.getCurrentPosition() < 150) {
//                    pivot.setPower(0.25);
//                }
//                pivot.setPower(0);
//                lock.setPosition(1);
//                mecanum.drift(0.5, 0, 1000);
//            }
//            ramp.setPower(-0.4);
//            pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            pivot.setPower(-0.4);
//            sleep(2000);
//            pivot.setPower(0);
//            ramp.setPower(0);
//
//            telemetry.addData("rings", numRings);
//            telemetry.update();
//            sleep(3000);
//
//            //ir.close();
            mecanum.reset();
        }
    }
}