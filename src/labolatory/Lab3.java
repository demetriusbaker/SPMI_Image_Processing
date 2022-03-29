package labolatory;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Scanner;

public class Lab3 {
    public static String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\german_hens.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static Mat sourceImage, changedImage;
    private static String windowTitle;
    private static final Scanner s = new Scanner(System.in);

    private static int savedPhoto = 0;

    public static void main(String[] args) {
        System.out.println("Welcome!");
        while (true) {
            System.out.println("What do you want to choose?");
            System.out.println("0) Exit from this program");
            System.out.println("1) Image reading");
            System.out.println("2) Image output on the screen");
            System.out.println("3) Converting from RGB to Gray");
            System.out.println("4) Affine transformations (moving; scaling; turn; shift)");
            System.out.println("5) Output of changed image on the screen");
            System.out.println("7) Saving of converted image in currently directory");

            switch (s.next()) {
                case "0" -> System.exit(0);
                case "1" -> sourceImage = readImage(path);
                case "2" -> outputImage("Source image", sourceImage);
                case "3" -> convertRGB2GRAY();
                case "4" -> doAffineTransformations();
                case "5" -> outputChangedImage();
                case "7" -> saveProcessedImageInDirectory();
                default -> System.out.println("Incorrect input!");
            }

            System.out.println();
        }
    }

    // 1
    private static Mat readImage(String path) {
        // TODO: add possibility to choose photo from directory!
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

            windowTitle = "Gray image";
            changedImage = grayImage;

            System.out.println("Converting RGB to GRAY was finished successfully!");
        } catch (Exception e) {
            System.out.println("Converting error: it cannot convert unread image!");
        }
    }

    // 4
    private static void doAffineTransformations() {
        try {
            AffineTransformation transformer = new AffineTransformation(sourceImage);

            System.out.println("Affine transformation examples");
            System.out.println("0) Exit from this menu");
            System.out.println("1) Moving");
            System.out.println("2) Scaling");
            System.out.println("3) Turning");
            System.out.println("4) Shifting");

            switch (s.next()) {
                case "0" -> {}
                case "1" -> printMovingExample(transformer);
                case "2" -> printScalingExample(transformer);
                case "3" -> printTurningExample(transformer);
                case "4" -> printShiftingExample(transformer);
                default -> System.out.println("Incorrect input!");
            }

            System.out.println();
        } catch (Exception exception) {
            System.out.println("You didn't install image!");
        }
    }

    // 4.1
    private static void printMovingExample(AffineTransformation transformer) {
        int movingNumber;

        System.out.println("Print moving coefficient (for ex: 0, 10, 100, -20 and other)");
        while (!s.hasNextInt())
            s.next();
        movingNumber = s.nextInt();

        windowTitle = "Moving on " + movingNumber;
        changedImage = transformer.move(movingNumber);
    }

    // 4.2
    private static void printScalingExample(AffineTransformation transformer) {
        double scale;

        System.out.println("Print scale (for ex: 1.4, 2.0, 0.3 and other)");
        while (!s.hasNextDouble())
            s.next();
        scale = s.nextDouble();

        windowTitle = "Scale: " + scale;
        changedImage = transformer.scale(scale);
    }

    // 4.3
    private static void printTurningExample(AffineTransformation transformer) {
        int flipCode;

        System.out.println("1) Turning examples with flip code parameter");
        System.out.println("2) Turning examples with degrees and scale");
        switch (s.next()) {
            case "1" -> {
                System.out.println("Output turning examples");
                System.out.println("1) Turning with 1 flip code");
                System.out.println("2) Turning with -1 flip code");
                System.out.println("any symbol) Turning with 0 flip code");
                switch (s.next()) {
                    case "1" -> flipCode = 1;
                    case "2" -> flipCode = -1;
                    default -> flipCode = 0;
                }

                windowTitle = "Turning with " + flipCode + " flip code";
                changedImage = transformer.turn(flipCode);
            }
            case "2" -> {
                double degree, scale;

                System.out.println("Print degree (for ex: 30, 45, 60, 90, 120 and other)");
                while (!s.hasNextDouble())
                    s.next();
                degree = s.nextDouble();

                System.out.println("Print scale (for ex: 1.4, 2.0, 0.3 and other)");
                while (!s.hasNextDouble())
                    s.next();
                scale = s.nextDouble();

                windowTitle = "" + degree + " degrees and " + scale + " scale";
                changedImage = transformer.turn(degree, scale);
            }
            default -> System.out.println("Incorrect input!");
        }
    }

    // 4.4
    private static void printShiftingExample(AffineTransformation transformer) {
        System.out.println("Print six values");

        double[] arr = new double[6];
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Input (" + (i + 1) + ") value");
            while (!s.hasNextDouble())
                s.next();
            arr[i] = s.nextDouble();
        }
        windowTitle = "Moving: " + arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4] + " " + arr[5];
        changedImage = transformer.shift(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
    }

    // 5
    private static void outputChangedImage() {
        outputImage(windowTitle, changedImage);
    }

    // 6
    private static void outputEveryChannelImageOnScreen() throws IOException {

    }

    // 7
    private static void saveProcessedImageInDirectory() {
        String imageName = "changed_image" + ++savedPhoto + ".jpg";

        String savedDirectory = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\saved\\" + imageName;
        Imgcodecs.imwrite(savedDirectory, changedImage);

        System.out.println("Image saved in currently directory!");
    }

    // 8
    private static void saveEveryChannelImageInDirectory() {

    }

    private static void showHighGUIWindows() {
        HighGui.waitKey(1);
        HighGui.destroyAllWindows();
    }
}
