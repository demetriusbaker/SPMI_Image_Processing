package labolatory;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Scanner;

public class Lab3 {
    public static String path = "E:\\Android_Development\\Java\\OpenCV\\src\\german_hens.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static Mat image;

    public static void main(String[] args){
        System.out.println("Welcome!");
        while (true) {
            System.out.println("What do you want to choose?");
            System.out.println("1) Image reading");
            System.out.println("2) Image output on the screen");
            System.out.println("3) Converting from RGB to Gray");
            System.out.println("e) Exit from this programm");

            Scanner s = new Scanner(System.in);
            switch (s.next()) {
                case "1" -> image = readImage(path);
                case "2" -> outputImage("Source image", image);
                case "3" -> convertRGB2GRAY();
                case "e" -> System.exit(0);
                default -> System.out.println("Incorrect input!");
            }

            System.out.println();
        }
    }

    // 1
    private static Mat readImage(String path) {
        Mat image = Imgcodecs.imread(path);

        if (image.empty())
            System.out.println("Oops, reading error!");
        else System.out.println("Image reading has been finished successfully!");

        return image;
    }

    // 2
    private static void outputImage(String title, Mat image) {
        try {
            HighGui.imshow(title, image);
            showHighGUIWindows();
        } catch (Exception e) {
            System.out.println("Output error: the image was read unsuccessfully!");
        }
    }

    // 3
    private static void convertRGB2GRAY() {
        try {
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY);
            outputImage("Gray image", grayImage);
        } catch (Exception e) {
            System.out.println("Converting error: it cannot convert unread image!");
        }
    }

    private static void showHighGUIWindows() {
        //  will display the window infinitely until any keypress (it is suitable for image display)
        HighGui.waitKey(0);
        // The function destroyAllWindows destroys all of the opened HighGUI windows
        HighGui.destroyAllWindows();
    }
}
