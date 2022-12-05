package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp
public class Drivercontrol extends LinearOpMode {
    // mecanum drive train
    private Mecanum mecanum;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    // wobble goal manipulation system
    private DcMotor pivot;
    private Servo lock;

    // ring manipulation system
    private DcMotor launch;
    private DcMotor ramp;
    private CRServo intake;



    @Override
    public void runOpMode() {
        // mecanum initialization
        mecanum = new Mecanum(hardwareMap.get(BNO055IMU.class, "imu"), hardwareMap.get(DcMotor.class, "frontLeft"), hardwareMap.get(DcMotor.class, "backLeft"), hardwareMap.get(DcMotor.class, "frontRight"), hardwareMap.get(DcMotor.class, "backRight"));
        mecanum.constantPower();

        // wobble goal manipulation system initialization
        pivot = hardwareMap.get(DcMotor.class, "pivot");
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lock = hardwareMap.get(Servo.class, "lock");

        // ring manipulation system initialization
        launch = hardwareMap.get(DcMotor.class, "launch");
        launch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ramp = hardwareMap.get(DcMotor.class, "ramp");
        ramp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake = hardwareMap.get(CRServo.class, "intake");



        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // mecanum drive
            mecanum.drive(gamepad1.right_trigger - gamepad1.left_trigger, gamepad1.left_stick_x, gamepad1.right_stick_x);


            // telemetry
            telemetry.addData("heading", mecanum.getHeading());
            telemetry.update();

            //pivot
            if (gamepad2.left_stick_y < -0.5 && pivot.getCurrentPosition() > 255) {
                pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                pivot.setPower(-0.25);
            } else if (gamepad2.left_stick_y > 0.5) {
                pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                pivot.setPower(0.25);
            } else {
                pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                pivot.setPower(-0.1);
            }

            // lock
            if (gamepad2.a) {
                lock.setPosition(1);
            } else {
                lock.setPosition(0);
            }


            if (gamepad1.x) {
                mecanum.reset();
            }
        }
    }
}