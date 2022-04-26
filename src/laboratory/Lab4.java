package laboratory;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class Lab4 extends JFrame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final int width = 1000, height = 800;
    private static final int imageWidth = 600, imageHeight = 600;

    private static String imageName;
    private static Mat sourceImage, convertedImage;

    private static JFrame frame;

    private static final String extension = ".jpg";
    private static final String windowName = "Image processing 2";

    private static Tools tools;

    private static Audio audio;

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle(windowName);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JLabel label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        frame.getContentPane().setBackground(Color.BLACK);
        frame.getContentPane().add(label);

        JMenuBar mb = new JMenuBar() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = Math.max(d.width, 1000);
                d.height = Math.max(d.height, 30);
                return d;
            }
        };
        mb.setVisible(true);
        frame.setJMenuBar(mb);

        JMenu menu = new JMenu("Image");
        JMenu realiseMenu = new JMenu("Realisation");
        JMenu histogramsMenu = new JMenu("Histograms");

        JMenuItem loadImage = new JMenuItem("Load image");
        JMenuItem resetImage = new JMenuItem("Reset image");
        JMenuItem saveImage = new JMenuItem("Save image");
        JMenuItem exit = new JMenuItem("Exit");

        JMenu preparation = new JMenu("Preparation");
        JMenuItem aPreparation = new JMenuItem("Prepare like A");
        JMenuItem bPreparation = new JMenuItem("Prepare like B");
        JMenuItem cPreparation = new JMenuItem("Prepare like C");
        JMenuItem dPreparation = new JMenuItem("Prepare like D");
        JMenuItem ePreparation = new JMenuItem("Prepare like E");
        JMenuItem fPreparation = new JMenuItem("Prepare like F");
        JMenuItem gPreparation = new JMenuItem("Prepare like G");
        JMenuItem hPreparation = new JMenuItem("Prepare like H");
        JMenuItem gauss = new JMenuItem("Gauss");
        JMenuItem canny = new JMenuItem("Canny");

        JMenuItem sourceHistograms = new JMenuItem("Build source histograms");
        JMenuItem convertedHistograms = new JMenuItem("Build converted histograms");

        JMenuItem[] otherJMenuItems = new JMenuItem[]{
                resetImage,
                saveImage,
                realiseMenu,
                histogramsMenu
        };

        mb.add(menu);
        mb.add(realiseMenu);
        mb.add(histogramsMenu);

        menu.add(loadImage);
        menu.add(resetImage);
        menu.add(saveImage);
        menu.add(exit);

        realiseMenu.add(preparation);
        preparation.add(aPreparation);
        preparation.add(bPreparation);
        preparation.add(cPreparation);
        preparation.add(dPreparation);
        preparation.add(ePreparation);
        preparation.add(fPreparation);
        preparation.add(gPreparation);
        preparation.add(hPreparation);
        realiseMenu.add(gauss);
        realiseMenu.add(canny);

        histogramsMenu.add(sourceHistograms);
        histogramsMenu.add(convertedHistograms);

        tools = new Tools(
                frame,
                label,
                otherJMenuItems,
                extension,
                imageWidth,
                imageHeight
        );

        tools.setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage());
        resetImage.addActionListener(e -> resetImage());
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> System.exit(0));

        aPreparation.addActionListener(e -> prepare(1));
        bPreparation.addActionListener(e -> prepare(2));
        cPreparation.addActionListener(e -> prepare(3));
        dPreparation.addActionListener(e -> prepare(4));
        ePreparation.addActionListener(e -> prepare(5));
        fPreparation.addActionListener(e -> prepare(6));
        gPreparation.addActionListener(e -> prepare(7));
        hPreparation.addActionListener(e -> prepare(8));
        gauss.addActionListener(e -> toGauss());
        canny.addActionListener(e -> toCanny());

        sourceHistograms.addActionListener(e -> buildHistograms(sourceImage));
        convertedHistograms.addActionListener(e -> {
            try {
                buildHistograms(convertedImage);
            } catch (NullPointerException exception) {
                audio = new Audio("src\\laboratory\\raw\\oh-shit-iam-sorry.wav", 0.8);
                audio.sound();

                String title = "Processing error";
                String errorMessage = "You should to apply any changes for this picture, " +
                        "then you can build histograms!";
                JOptionPane.showMessageDialog(
                        null,
                        exception + "!\n" + errorMessage,
                        title,
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (CvException cvException) {
                audio = new Audio("src\\laboratory\\raw\\fuck-you1.wav", 0.8);
                audio.sound();

                JOptionPane.showMessageDialog(
                        null,
                        "What's the hell?!\n" +
                                cvException + "\n" +
                                cvException.getMessage() + "\n",
                        "Oh shit error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private static void loadImage() {
        FileDialog dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setVisible(true);

        String directory = dialog.getDirectory();
        String file = dialog.getFile();
        String fileName = directory + file;

        if (directory == null || file == null) {
            audio = new Audio("src\\laboratory\\raw\\attention.wav", 0.8);
            audio.sound();

            JOptionPane.showMessageDialog(
                    null,
                    "You didn't choose anything jpeg file!\n",
                    "Choose jpeg image",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        if (!file.contains(extension)) {
            audio = new Audio("src\\laboratory\\raw\\oh-shit-iam-sorry.wav", 0.8);
            audio.sound();

            JOptionPane.showMessageDialog(
                    null,
                    "The file of wrong extension!\n" +
                            "Check please extension!",
                    "Extension error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (!(tools.isValidISOLatin1(fileName))) {
            audio = new Audio("src\\laboratory\\raw\\woo.wav", 1.0);
            audio.sound();

            JOptionPane.showMessageDialog(
                    null,
                    "The name of file is wrong!\n" +
                            "Please check name and use only latin alphabet!",
                    "Name error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        sourceImage = Imgcodecs.imread(fileName);
        tools.resized = false;
        tools.setImage(sourceImage, true);
        tools.setLockOtherMenu(false);

        imageName = dialog.getFile();

        convertedImage = null;
    }

    private static void resetImage() {
        convertedImage = sourceImage;
        tools.setImage(convertedImage, false);
    }

    private static void saveImage() {
        Mat mat;
        if (convertedImage == null) mat = sourceImage;
        else mat = convertedImage;

        if (sourceImage == null) return;

        try {
            FileDialog dialog = new FileDialog(frame, "Save as", FileDialog.SAVE);
            dialog.setFile(imageName);
            dialog.setVisible(true);

            String savedDirectory = dialog.getDirectory() + dialog.getFile();
            Imgcodecs.imwrite(savedDirectory, mat);
        } catch (Exception ignored) {
        }
    }

    // TODO: preparing, gauss, canny and fucking masks!!!
    private static void prepare(int code) {
        switch (code) {
            case 1:
                System.out.println();
                break;
            case 2:
                System.out.println();
                break;
            case 3:
                System.out.println();
                break;
            case 4:
                System.out.println();
                break;
            case 5:
                System.out.println();
                break;
            case 6:
                System.out.println();
                break;
            case 7:
                System.out.println();
                break;
            default:
                break;
        }
    }

    private static void toGauss() {

    }

    private static void toCanny() {
        Mat dst = new Mat();
        Mat gray = new Mat();
        Mat image = new Mat();

        Imgproc.GaussianBlur(sourceImage, dst, new Size(3, 3), 5, 5);
        Imgproc.cvtColor(dst, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, image, 0, 0, 3, false);

        convertedImage = image;

        tools.setImage(convertedImage, false);
    }

    private static void buildHistograms(Mat image) {
        Size imageSize = image.size();

        // Set the amount of bars in the histogram.
        int histSize = 256;
        MatOfInt histogramSize = new MatOfInt(histSize);

        // Set the height of the histogram and width of the bar.
        int histogramHeight = (int) imageSize.height;
        int binWidth = 5;

        // Set the value range.
        MatOfFloat histogramRange = new MatOfFloat(0f, 256f);

        // Create two separate lists: one for colors and one for channels (these will be used as separate datasets).
        Scalar[] colorsRgb = new Scalar[]{
                new Scalar(255, 0, 0, 255),
                new Scalar(0, 255, 0, 255),
                new Scalar(0, 0, 255, 255)};
        MatOfInt[] channels = new MatOfInt[]{
                new MatOfInt(0),
                new MatOfInt(1),
                new MatOfInt(2)
        };

        // Create an array to be saved in the histogram and a second array, on which the histogram chart will be drawn.
        Mat[] histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
        Mat histMatBitmap = new Mat(imageSize, image.type());

        // Separate channels
        for (int i = 0; i < channels.length; i++) {
            Imgproc.calcHist(
                    Collections.singletonList(image),
                    channels[i],
                    new Mat(),
                    histograms[i],
                    histogramSize, histogramRange
            );
            Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
            for (int j = 0; j < histSize; j++) {
                Point p1 = new Point(
                        binWidth * (j - 1),
                        histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0])
                );
                Point p2 = new Point(
                        binWidth * j,
                        histogramHeight - Math.round(histograms[i].get(j, 0)[0])
                );
                Imgproc.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
            }

            for (int j = 0; j < histSize; j++) {
                Point p1 = new Point(
                        binWidth * (j - 1),
                        histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0])
                );
                Point p2 = new Point(
                        binWidth * j,
                        histogramHeight - Math.round(histograms[i].get(j, 0)[0])
                );
                Imgproc.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
            }
        }

        Mat loh = new Mat();
        Imgproc.cvtColor(histMatBitmap, loh, Imgproc.COLOR_RGB2GRAY);
        showHistogramWindow(histMatBitmap);
    }

    private static void showHistogramWindow(Mat mat) {
        int width = 900, height = 500;
        int imageWidth = 900, imageHeight = 500;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle("Histograms");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JLabel label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        frame.getContentPane().add(label);

        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());
        ic = new ImageIcon(ic.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }
}
