package labolatory;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Scanner;

public class Lab3 {
    public static String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\german_hens.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static Mat sourceImage;

    public static void main(String[] args) {
        System.out.println("Welcome!");
        while (true) {
            System.out.println("What do you want to choose?");
            System.out.println("0) Exit from this program");
            System.out.println("1) Image reading");
            System.out.println("2) Image output on the screen");
            System.out.println("3) Converting from RGB to Gray");
            System.out.println("4) Affine transformations (moving; scaling; turn; shift)");

            Scanner s = new Scanner(System.in);
            switch (s.next()) {
                case "0" -> System.exit(0);
                case "1" -> sourceImage = readImage(path);
                case "2" -> outputImage("Source image", sourceImage);
                case "3" -> convertRGB2GRAY();
                case "4" -> doAffineTransformations();
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
            Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);
            outputImage("Gray image", grayImage);
        } catch (Exception e) {
            System.out.println("Converting error: it cannot convert unread image!");
        }
    }

    // 4
    private static void doAffineTransformations() {
        boolean isExit = false;

        if (sourceImage == null) {
            System.out.println("You didn't install image!");
            isExit = true;
        }

        AffineTransformation transformer = new AffineTransformation(sourceImage);

        while (!isExit) {
            System.out.println("Affine transformation examples");
            System.out.println("0) Exit from this menu");
            System.out.println("1TODO) Moving");
            System.out.println("2) Scaling");
            System.out.println("3) Turning");
            System.out.println("4) Shifting");

            Scanner s = new Scanner(System.in);
            switch (s.next()) {
                case "0" -> isExit = true;
                case "1" -> {
                    outputImage("Moving", transformer.move());
                }
                case "2" -> {
                    outputImage("Scale: 3.0", transformer.scale(3.0));
                    outputImage("Scale: 2.0", transformer.scale(2.0));
                    outputImage("Scale: 1.5", transformer.scale(1.5));
                    outputImage("Scale: 1.0", transformer.scale(1.0));
                    outputImage("Scale: 0.5", transformer.scale(0.5));
                    outputImage("Scale: 0.2", transformer.scale(0.2));
                    outputImage("Scale: 0.1", transformer.scale(0.1));
                }
                case "3" -> {
                    s = new Scanner(System.in);
                    System.out.println("1) Turning examples with flip code parameter");
                    System.out.println("2) Turning examples with degrees and scale");
                    switch (s.next()) {
                        case "1" -> {
                            System.out.println("Output turning examples");
                            outputImage("Turning with 0 flip code", transformer.turn(0));
                            outputImage("Turning with 1 flip code", transformer.turn(1));
                            outputImage("Turning with -1 flip code", transformer.turn(-1));
                        }
                        case "2" -> {
                            System.out.println("Output turning examples with parameters");
                            outputImage("30 degrees and 1 scale", transformer.turn(30, 1));
                            outputImage("45 degrees and 0.5 scale", transformer.turn(45, 0.5));
                            outputImage("60 degrees and 1.5 scale", transformer.turn(60, 1.5));
                            outputImage("90 degrees and 2.0 scale", transformer.turn(90, 2));
                            outputImage("120 degrees and 0.1 scale", transformer.turn(120, 0.1));
                        }
                        default -> System.out.println("Incorrect input!");
                    }
                }
                case "4" -> {
                    outputImage("Moving", transformer.shift());
                }
                default -> System.out.println("Incorrect input!");
            }

            System.out.println();
        }
    }

    private static void showHighGUIWindows() {
        HighGui.waitKey(1);
        HighGui.destroyAllWindows();
    }
}
