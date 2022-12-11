package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp
public class Drivercontrol extends LinearOpMode {

    private DcMotor linearSlideMotor;
    private Mecanum mecanum;
    private Servo claw;
//    private boolean oldState = false;
    private boolean currentState;
//    private boolean clawAction = false;

    @Override
    public void runOpMode() throws InterruptedException {

        mecanum = new Mecanum(
                hardwareMap.get(BNO055IMU.class, "imu"),
                hardwareMap.get(DcMotor.class, "frontLeft"),
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "backRight"),
                hardwareMap.get(DcMotor.class, "backLeft")
        );
        mecanum.constantPower();
        linearSlideMotor = hardwareMap.get(DcMotor.class, "linearSlide");
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        claw = hardwareMap.get(Servo.class, "clawServo");
        claw.setPosition(0.4); // 0 is open
        waitForStart();

        while (opModeIsActive()) {
            //linear  slide controls
            if (Math.abs(gamepad2.right_stick_y)<0.1) {
                linearSlideMotor.setPower(0);
            } else {
                linearSlideMotor.setPower(0.55*gamepad2.right_stick_y);
            }
            //claw

            currentState= gamepad2.a;
            if(currentState){
                claw.setPosition(0.1); // closed
            }
            else{
                claw.setPosition(0.3);
            }
//            (!oldState && currentState){
//                clawAction = !clawAction;
//            }
//            oldState= currentState;
//            if(clawAction){
//                claw.setPosition(0.4); // closed
//            } else {
//                claw.setPosition(0.1); // open
//            }
            //driving
            if (gamepad1.x) {
                mecanum.brake(10);
            } else if (gamepad1.a) {
                mecanum.drive( 0.5 * (gamepad1.right_trigger - gamepad1.left_trigger),
                        0.5 * (gamepad1.left_stick_x), 0.5 * (gamepad1.right_stick_x));
            } else {
                mecanum.drive(gamepad1.right_trigger - gamepad1.left_trigger,
                        gamepad1.left_stick_x, gamepad1.right_stick_x);
            }
        }
    }
}
