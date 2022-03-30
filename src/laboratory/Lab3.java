package laboratory;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Lab3 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Change your directory!
    private static final String sourcePath = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\laboratory\\source";
    private static final String savedPath = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\laboratory\\saved";

    private static String windowTitle, imageName;

    private static Mat sourceImage, changedImage;
    private static Mat[] channelImages;

    private static final Scanner s = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        while (true) {
            System.out.println("What do you want to choose?");
            System.out.println("0) Exit from this program");
            System.out.println("1) Image reading");
            System.out.println("2) Image output on the screen");
            System.out.println("3) Converting from RGB to Gray");
            System.out.println("4) Affine transformations (moving; scaling; turn; shift)");
            System.out.println("5) Output of changed image on the screen");
            System.out.println("6) Output every channel of image on the screen");
            System.out.println("7) Saving of converted image in currently directory");
            System.out.println("8) Saving of every channel of image in currently directory");

            switch (s.next()) {
                case "0" -> System.exit(0);
                case "1" -> sourceImage = readImage();
                case "2" -> outputImage("Source image", sourceImage);
                case "3" -> convertRGB2GRAY();
                case "4" -> doAffineTransformations();
                case "5" -> outputChangedImage();
                case "6" -> outputEveryChannelImageOnScreen();
                case "7" -> saveProcessedImageInDirectory();
                case "8" -> saveEveryChannelImageInDirectory();
                default -> System.out.println("Incorrect input!");
            }

            System.out.println();
        }
    }

    // 1 чтение изображения
    private static Mat readImage() {
        try {
            System.out.println("Choose picture from this list:");

            File dir = new File(sourcePath);
            File[] arrFiles = dir.listFiles();
            if (arrFiles != null) {
                String[] photoLinks = new String[arrFiles.length];

                System.out.println("0) Exit");
                for (int i = 0; i < arrFiles.length; i++) {
                    photoLinks[i] = arrFiles[i].getAbsolutePath();
                    System.out.println(i + 1 + ") " + photoLinks[i]);
                }

                int number;

                do {
                    while (!s.hasNextInt())
                        s.next();
                    number = s.nextInt();
                } while (number < 0 || number > photoLinks.length);

                if (number == 0) return null;
                System.out.println("The image was chosen!");

                imageName = photoLinks[number - 1];

                return Imgcodecs.imread(imageName);
            } else {
                System.out.println("Unfortunately, this list is empty!");
                return null;
            }
        } catch (NullPointerException nullPointerException) {
            System.out.println("Image loading failed!");
            return null;
        }
    }

    // 2 вывод изображения на экран
    private static void outputImage(String title, Mat image) {
        try {
            HighGui.imshow(title, image);
            showHighGUIWindows();
        } catch (Exception e) {
            System.out.println("Output error: the image was read unsuccessfully!");
        }
    }

    // 3 преобразование из RGB в Gray
    private static void convertRGB2GRAY() {
        try {
            Mat grayImage = new Mat();
            Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);

            windowTitle = "gray_image";
            changedImage = grayImage;

            System.out.println("Converting RGB to GRAY was finished successfully!");
        } catch (Exception e) {
            System.out.println("Converting error: it cannot convert unread image!");
        }
    }

    // 4 аффинные преобразования (перемещение; масштабирование; поворот; сдвиг)
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
                case "0" -> {
                }
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

    // 4.1 перемещение
    private static void printMovingExample(AffineTransformation transformer) {
        int movingNumber;

        System.out.println("Print moving coefficient (for ex: 0, 10, 100, -20 and other)");
        while (!s.hasNextInt())
            s.next();
        movingNumber = s.nextInt();

        windowTitle = "moving on " + movingNumber;
        changedImage = transformer.move(movingNumber);
    }

    // 4.2 масштабирование
    private static void printScalingExample(AffineTransformation transformer) {
        double scale;

        System.out.println("Print scale (for ex: 1.4, 2.0, 0.3 and other)");
        while (!s.hasNextDouble())
            s.next();
        scale = s.nextDouble();

        windowTitle = "scale: " + scale;
        changedImage = transformer.scale(scale);
    }

    // 4.3 поворот
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

                windowTitle = "turning with " + flipCode + " flip code";
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

    // 4.4 сдвиг
    private static void printShiftingExample(AffineTransformation transformer) {
        System.out.println("Print six values");

        double[] arr = new double[6];
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Input (" + (i + 1) + ") value");
            while (!s.hasNextDouble())
                s.next();
            arr[i] = s.nextDouble();
        }
        windowTitle = "shifting: " + arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4] + " " + arr[5];
        changedImage = transformer.shift(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
    }

    // 5 вывод преобразованного изображения на экран
    private static void outputChangedImage() {
        outputImage(windowTitle, changedImage);
    }

    // 6 вывод каждого канала изображения на экран
    private static void outputEveryChannelImageOnScreen() {
        try {
            channelImages = new Mat[]{
                    getPictureWithChangedChannel(0, 0, 0, false),
                    getPictureWithChangedChannel(255, 0, 0, false),
                    getPictureWithChangedChannel(0, 255, 0, false),
                    getPictureWithChangedChannel(0, 0, 255, false),
                    getPictureWithChangedChannel(255, 255, 0, false),
                    getPictureWithChangedChannel(255, 0, 255, false),
                    getPictureWithChangedChannel(0, 255, 255, false),
                    getPictureWithChangedChannel(255, 255, 255, false),
                    getPictureWithChangedChannel(0, 0, 0, true),
                    getPictureWithChangedChannel(255, 0, 0, true),
                    getPictureWithChangedChannel(0, 255, 0, true),
                    getPictureWithChangedChannel(0, 0, 255, true),
                    getPictureWithChangedChannel(255, 255, 0, true),
                    getPictureWithChangedChannel(255, 0, 255, true),
                    getPictureWithChangedChannel(0, 255, 255, true),
                    getPictureWithChangedChannel(255, 255, 255, true)
            };
        } catch (Exception e) {
            System.out.println("Error! You need to load an image!");
        }
    }

    // 7 сохранение преобразованного изображения в заданной директории
    private static void saveProcessedImageInDirectory() {
        try {
            imageName = imageName.replace(sourcePath, "");

            String savedDirectory = savedPath + "\\" + imageName;
            Imgcodecs.imwrite(savedDirectory, changedImage);

            System.out.println("Image saved in currently directory!");
        } catch (Exception e) {
            System.out.println("It couldn't download the image, because it's empty!");
        }
    }

    // 8 сохранение каждого канала изображения в заданной директории
    private static void saveEveryChannelImageInDirectory() {
        try {
            String channel = "channel", jpg = ".jpg";
            String channelFolderName = imageName.replace(jpg, "_").replace(sourcePath, savedPath) + channel;
            Path path = Paths.get(channelFolderName);

            try {
                Files.createDirectory(path);
            } catch (java.nio.file.FileAlreadyExistsException ignored) {
            }


            for (int i = 0; i < channelImages.length; i++) {
                try {
                    String savedDirectory = channelFolderName + "\\" + (i + 1) + jpg;
                    Imgcodecs.imwrite(savedDirectory, channelImages[i]);
                } catch (Exception e) {
                    System.out.println("This file is exist already!");
                }
            }
        } catch (Exception e) {
            System.out.println("Caused error! Choose 6) to create channels!");
        }
    }

    // show gui windows
    private static void showHighGUIWindows() {
        HighGui.waitKey(1);
        HighGui.destroyAllWindows();
    }

    // change color channel and get image
    private static Mat getPictureWithChangedChannel(int red, int green, int blue, boolean reverseMode) {
        Mat mat = new Mat(sourceImage.size(), sourceImage.type());
        int channels = sourceImage.channels();
        double[] pixel;
        for (int i = 0, r_length = sourceImage.rows(); i < r_length; i++) {
            for (int j = 0, c_length = sourceImage.cols(); j < c_length; j++) {
                if (channels == 3) {
                    pixel = sourceImage.get(i, j).clone();
                    pixel[0] = pixel[0] - red;
                    pixel[1] = pixel[1] - green;
                    pixel[2] = pixel[2] - blue;

                    if (reverseMode) {
                        pixel[0] = 255 - pixel[0];
                        pixel[1] = 255 - pixel[1];
                        pixel[2] = 255 - pixel[2];
                    }
                    mat.put(i, j, pixel);
                } else {
                    mat.put(i, j, sourceImage.get(i, j).clone());
                }
            }
        }

        outputImage("Changed channels", mat);

        return mat;
    }
}
