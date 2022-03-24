package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Arrays;

public class Chapter3 {
    public static String path = "E:\\Android_Development\\Java\\OpenCV\\src\\com\\company\\mouse.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {
        Mat img = Imgcodecs.imread(path);
        if (img.empty())
            System.out.println("Failed image downloading!");
        else {
            System.out.println(img.width() + " x " + img.height());
            System.out.println(CvType.typeToString(img.type()));
            System.out.println(img.channels());
        }

        System.out.println(Imgcodecs.IMREAD_UNCHANGED); // -1
        System.out.println(Imgcodecs.IMREAD_GRAYSCALE); // 0
        System.out.println(Imgcodecs.IMREAD_COLOR); // 1
        System.out.println(Imgcodecs.IMREAD_ANYDEPTH); // 2
        System.out.println(Imgcodecs.IMREAD_ANYCOLOR); // 4
        System.out.println(Imgcodecs.IMREAD_LOAD_GDAL); // 8
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2); // 16
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_4); // 32
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_8); // 64
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_2); // 17
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_4); // 33
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_8); // 65
        System.out.println(Imgcodecs.IMREAD_IGNORE_ORIENTATION); // 128

        boolean st = Imgcodecs.imwrite(path, img);
        System.out.println(st);

        Mat m = new Mat(3, 2, CvType.CV_8UC1);
        double n = 1.0;
        for (int i = 0, r = m.rows(); i < r; i++) {
            for (int j = 0, c = m.cols(); j < c; j++) {
                m.put(i, j, n++);
            }
        }
        byte[] arr = new byte[m.channels() * m.cols() * m.rows()];
        System.out.println(m.get(0, 0, arr)); // 6
        System.out.println(Arrays.toString(arr)); // [1, 2, 3, 4, 5, 6]

        MatOfByte buf = new MatOfByte();
        st = Imgcodecs.imencode(".jpg", img, buf, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));

        System.out.println(st);

        Mat img2 = Imgcodecs.imdecode(buf, Imgcodecs.IMREAD_COLOR);
        System.out.println(img2);

        Mat matImg = Imgcodecs.imread(path);
        if (matImg.empty()) {
            System.out.println("OOOps, empty!");
            return;
        }
        CvUtils.showImage(matImg, "Hallo, mein bruders!");
    }



    // 115 / 312
}
