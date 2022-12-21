package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class EncoderTest extends LinearOpMode {
    DcMotor motor1;

    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class,"motor");
        waitForStart();
        while(opModeIsActive()){
            motor1.setPower(gamepad1.left_stick_y);
            telemetry.addData("position", motor1.getCurrentPosition());
            telemetry.update();

        }
    }

}
