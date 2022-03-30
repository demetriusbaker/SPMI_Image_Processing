package laboratory;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Scanner;

public class Lab4 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String sourcePath = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\laboratory\\source";
    private static final String savedPath = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\laboratory\\saved";

    private static String windowTitle, imageName;

    private static Mat sourceImage, changedImage;
    private static Mat[] channelImages;

    private static final Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome!");
        while (true) {
            System.out.println("What do you want to choose?");
            System.out.println("0) Exit from this program");
            System.out.println("1) Image reading");
            System.out.println("2) Image output on the screen");
            System.out.println("3) Function realization of image preparing for next properties");
            System.out.println("4) Hays function realization with setting faces possibility and main parameters");
            System.out.println("5) Realization of Connie's edge detector with setting main parameters");
            System.out.println("6) Histogram buildings of images");
            System.out.println("7) Output of converted image and its histogram on the screen");
            System.out.println("8) Saving of converted image in currently directory");

            switch (s.next()) {
                case "0" -> System.exit(0);
                case "1" -> sourceImage = readImage();
                case "2" -> outputImageOnScreen("Source image", sourceImage);
                case "3" -> realiseImagePreparation();
                case "4" -> realiseGaussFilter();
                case "5" -> realiseConnieEdgeDetector();
                case "6" -> buildImageHistogram();
                case "7" -> outputConvertedImageAndHistograms();
                case "8" -> saveConvertedImageInCurrentlyDirectory();
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
    private static void outputImageOnScreen(String title, Mat image) {
        try {
            HighGui.imshow(title, image);
            showHighGUIWindows();
        } catch (Exception e) {
            System.out.println("Output error: the image was read unsuccessfully!");
        }
    }

    // 3 реализация функции препарирование изображения для следующих характеристик
    private static void realiseImagePreparation() {

    }

    // 4 реализация функции фильтра Гаусса с возможностью задания маски и основных параметров
    private static void realiseGaussFilter() {

    }

    // 5 реализация детектора границ Connie с возможностью задания маски и основных параметров
    private static void realiseConnieEdgeDetector() {
        Imgproc.cvtColor(sourceImage, sourceImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(sourceImage, sourceImage, new Size(2, 2));

        double lowThresh = 100, highThresh = 300;
        Imgproc.Canny(sourceImage, sourceImage, lowThresh, highThresh);

        outputImageOnScreen("", sourceImage);
    }

    // 6 построения гистограмм изображений (исходных и преобразованных)
    private static void buildImageHistogram() {

    }

    // 7 вывод преобразованного изображения и его гистограммы на экран
    private static void outputConvertedImageAndHistograms() {
        outputImageOnScreen("Converted image", changedImage);
    }

    // 8 сохранение преобразованного изображения в заданной директории
    private static void saveConvertedImageInCurrentlyDirectory() {
        try {
            imageName = imageName.replace(sourcePath, "");

            String savedDirectory = savedPath + "\\" + imageName;
            Imgcodecs.imwrite(savedDirectory, changedImage);

            System.out.println("Image saved in currently directory!");
        } catch (Exception e) {
            System.out.println("It couldn't download the image, because it's empty!");
        }
    }

    private static void showHighGUIWindows() {
        HighGui.waitKey(1);
        HighGui.destroyAllWindows();
    }
}
