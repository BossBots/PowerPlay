package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
This program is for testing ONLY. It is TeleOp.
This program is for testing ComputerVision. It prints avgRGB within the targetted region and recognition to the screen.
*/


@TeleOp
public class AutonLeft extends LinearOpMode {

    private ComputerVision cv;
    private int recognition;
    private int[] avgRGB;

    @Override
    public void runOpMode() throws InterruptedException {

        cv = new ComputerVision(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                recognition = cv.getRecognition();   // recognition detected by ComputerVision
                avgRGB = cv.getRGB();   // getting avg RGB values in the region

                telemetry.addData("recognition", recognition);
                telemetry.addData("avg R", avgRGB[0]);
                telemetry.addData("avg G", avgRGB[1]);
                telemetry.addData("avg B", avgRGB[2]);
                telemetry.update();
            }
        }
    }
}
