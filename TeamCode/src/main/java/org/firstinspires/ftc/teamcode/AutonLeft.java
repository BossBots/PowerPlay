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

        // wobble goal system
        pivot = hardwareMap.get(DcMotor.class, "pivot");
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lock = hardwareMap.get(Servo.class, "lock");

        // ring manipulation system
        launch = hardwareMap.get(DcMotor.class, "launch");
        launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ramp = hardwareMap.get(DcMotor.class, "ramp");
        ramp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ramp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake = hardwareMap.get(CRServo.class, "intake");
        ramp_rings1 = hardwareMap.get(CRServo.class, "ramp_rings1");
        ramp_rings2 = hardwareMap.get(CRServo.class, "ramp_rings2");

        // image recognition system
        //ir = new ImageRecognition(hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        //ir.initializeSystem();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {

            // initial setup with rings on ramp and wobble goal in lock, resting on bracket
            ramp.setPower(0.15);
            lock.setPosition(0);
            launch.setPower(1);
            intake.setPower(-1);

            // wobble goal bending
            while (pivot.getCurrentPosition() < 100) {
                pivot.setPower(0.25);
            }
            while (pivot.getCurrentPosition() > 300) {
                pivot.setPower(-0.25);
            }
            pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pivot.setPower(-0.1);

            sleep(1000);

            // launch the rings!
            ramp_rings1.setPower(-0.75);
            ramp_rings2.setPower(-0.1);
            sleep(500);
            ramp_rings1.setPower(0);
            ramp_rings2.setPower(0);
            mecanum.drift(0.25, 0, 500);
            ramp_rings1.setPower(-0.75);
            ramp_rings2.setPower(-0.2);
            sleep(1500);
            ramp_rings1.setPower(0);
            ramp_rings2.setPower(0);
            mecanum.drift(0.25, 0, 500);
            ramp_rings1.setPower(-0.75);
            ramp_rings2.setPower(-0.25);
            sleep(1500);
            ramp_rings1.setPower(0);
            ramp_rings2.setPower(0);
            launch.setPower(0);
            ramp.setPower(-0.2);
            mecanum.drift(-0.25, 0, 1000);

            // lift pivot
            pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            pivot.setPower(-0.25);
            sleep(750);
            pivot.setPower(0);

            // move to ring sample
            mecanum.drift(-0.25, 0, 750);
            ramp.setPower(0.1);
            mecanum.forward(0.4, 0, 750);
            mecanum.brake(500);

            // Object Detection
            //numRings = ir.getRecognition();

            telemetry.addData("rings", numRings);
            telemetry.update();
            sleep(1000);

            // place the 1st wobble goal
            mecanum.drift(0.5, 0, 500);
            if (numRings.equals("Quad")) {
                mecanum.forward(0.6, 0, 2000);
                mecanum.drift(-0.5, 0, 750);
                while (pivot.getCurrentPosition() < 150) {
                    pivot.setPower(0.25);
                }
                pivot.setPower(0);
                lock.setPosition(1);
                mecanum.drift(0.5, 0, 750);
                mecanum.forward(-0.5, 0, 1250);
            } else if (numRings.equals("Single")) {
                mecanum.forward(0.6, 0, 1750);
                //mecanum.drift(-0.5, 0, 350);
                while (pivot.getCurrentPosition() < 150) {
                    pivot.setPower(0.25);
                }
                pivot.setPower(0);
                lock.setPosition(1);
                mecanum.drift(0.5, 0, 250);
                mecanum.forward(-0.5, 0, 750);
            } else {
                mecanum.forward(0.6, 0, 1250);
                mecanum.drift(-0.5, 0, 750);
                while (pivot.getCurrentPosition() < 150) {
                    pivot.setPower(0.25);
                }
                pivot.setPower(0);
                lock.setPosition(1);
                mecanum.drift(0.5, 0, 1000);
            }
            ramp.setPower(-0.4);
            pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            pivot.setPower(-0.4);
            sleep(2000);
            pivot.setPower(0);
            ramp.setPower(0);

            telemetry.addData("rings", numRings);
            telemetry.update();
            sleep(3000);

            //ir.close();
            mecanum.reset();
        }
    }
}