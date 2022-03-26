package com.company.imageanalysisprocessing;

import com.company.imagedownloading.CvUtils;
import com.company.imagedownloading.CvUtilsFX;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Chapter4 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {
        Mat image = new Mat(300, 300, CvType.CV_8UC3, CvUtils.COLOR_WHITE);
        Imgproc.line(
                image,
                new Point(50, 50),
                new Point(250, 50),
                CvUtils.COLOR_RED
        );
        Imgproc.putText(
                image,
                "OpenCV",
                new Point(10, 20),
                6,
                1,
                CvUtils.COLOR_BLACK
        );

        String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\com\\company\\mouse.jpg";

        Mat matImg = Imgcodecs.imread(path);
        if (matImg.empty()) {
            System.out.println("OOOps, empty!");
            return;
        }
        CvUtils.showImage(matImg, "Hallo, mein bruders!");

    }
}
