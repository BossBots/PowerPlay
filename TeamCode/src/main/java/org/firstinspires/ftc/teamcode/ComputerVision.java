package org.firstinspires.ftc.teamcode;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

public class ComputerVision {

    private OpenCvCamera phoneCam;

    // change matrix region to whatever is best visible
    // width of image is 640
    // height of image is 480
    private final int[][] region =  {{320 - 70, 240 - 20}, {320 + 70, 240 + 20}};//new int[2][2];
    /*region[0][0] = 320 - 70;    // top left x
    region[0][1] = 240 - 20;    // top left y
    region[1][0] = 320 + 70;    // bottom right x
    region[1][1] = 240 + 20;    // bottom right y*/

    // change these colors to whatever you select. make sure you choose appropriate min and max thresholds
    private final int[][] color1 = {{100, 0, 100}, {255, 100, 255}};
    /*color1[0][0] = 100; // color1 min R
    color1[0][1] = 0;   // color1 min G
    color1[0][2] = 100; // color1 min B
    color1[1][0] = 255; // color1 max R
    color1[1][1] = 100; // color1 min G
    color1[1][2] = 255; // color1 max B*/

    private final int[][] color2 = {{0, 100, 0}, {100, 255, 100}};
    /*color2[0][0] = 0;   // color2 min R
    color2[0][1] = 100; // color2 min G
    color2[0][2] = 0;   // color2 min B
    color2[1][0] = 100; // color2 max R
    color2[1][1] = 255; // color2 max G
    color2[1][2] = 100; // color2 max B*/

    private final int[][] color3 = {{100, 75, 0}, {255, 200, 100}};
    /*color3[0][0] = 100; // color3 min R
    color3[0][1] = 75;  // color3 min G
    color3[0][2] = 0;   // color3 min B
    color3[1][0] = 255; // color3 max R
    color3[1][1] = 200; // color3 max G
    color3[1][2] = 100; // color3 max B*/

    private int[] avgRGB = new int[3];
    private int recognition;

    public ComputerVision(int camId) {
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, camId);
        phoneCam.setPipeline(new ComputerVision.Pipeline());
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {
            }
        });
    }

    public boolean isColor(int[][] color) {
        return (
                color[0][0] <= avgRGB[0] && avgRGB[0] <= color[1][0] &&
                        color[0][1] <= avgRGB[1] && avgRGB[1] <= color[1][1] &&
                        color[0][2] <= avgRGB[2] && avgRGB[2] <= color[1][2]
        );
    }

    public void analyze() {
        if (isColor(color1)) {
            recognition = 1;
        } else if (isColor(color2)) {
            recognition = 2;
        } else {
            recognition = 3;
        }
    }

    public int getRecognition() {
        return recognition;
    }

    public int[] getRGB() {
        return avgRGB;
    }

    class Pipeline extends OpenCvPipeline {

        private boolean viewportPaused = false;
        private Mat YCrCb = new Mat();
        private Mat Cb = new Mat();
        private Mat regionMatrix;

        private void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 2);
        }

        @Override
        public void init(Mat firstFrame) {
            inputToCb(firstFrame);
            regionMatrix = firstFrame.submat(new Rect(
                    new Point(region[0][0], region[0][1]),
                    new Point(region[1][0], region[1][1])
            ));
        }

        @Override
        public Mat processFrame(Mat input) {
            regionMatrix = input.submat(new Rect(
                    new Point(region[0][0], region[0][1]),
                    new Point(region[1][0], region[1][1])
            ));
            double[] avg;
            avg = Core.mean(regionMatrix).val;
            for (int i = 0; i < 3; i++) {
                avgRGB[i] = (int) avg[i];
            }
            Imgproc.rectangle(
                    input,
                    new Point(region[0][0], region[0][1]),
                    new Point(region[1][0], region[1][1]),
                    new Scalar(0, 0, 255),
                    4
            );
            analyze();
            return input;
        }

        @Override
        public void onViewportTapped() {
            viewportPaused = !viewportPaused;
            if (viewportPaused) {
                phoneCam.pauseViewport();
            } else {
                phoneCam.resumeViewport();
            }
        }
    }
}
