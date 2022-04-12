package laboratory.lab4;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;

public class Lab4 extends JFrame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Version - " + Core.VERSION);
    }

    private static final String interimPath = "src/laboratory/lab4/image.jpg";

    private static final int width = 800, height = 600;
    private static final int imageWidth = 600, imageHeight = 500;

    private static String imageName;
    private static Mat sourceImage, convertedImage;

    private static JFrame frame;
    private static JLabel label;

    private static JMenuItem[] otherJMenuItems;

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle("Fisting is 300$ bucks");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

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
        JMenuItem saveImage = new JMenuItem("Save image");
        JMenuItem exit = new JMenuItem("Exit");

        JMenuItem preparation = new JMenuItem("Preparation");
        JMenuItem gauss = new JMenuItem("Gauss");
        JMenuItem canny = new JMenuItem("Canny");

        JMenuItem histogram = new JMenuItem("Build histograms");

        otherJMenuItems = new JMenuItem[]{
                saveImage,
                realiseMenu,
                histogramsMenu
        };

        mb.add(menu);
        mb.add(realiseMenu);
        mb.add(histogramsMenu);

        menu.add(loadImage);
        menu.add(saveImage);
        menu.add(exit);

        realiseMenu.add(preparation);
        realiseMenu.add(gauss);
        realiseMenu.add(canny);

        histogramsMenu.add(histogram);

        setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage());
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> exit());

        preparation.addActionListener(e -> prepare());
        gauss.addActionListener(e -> toGauss());
        canny.addActionListener(e -> toCanny());

        histogram.addActionListener(e -> {
            buildHistograms("Source histograms", sourceImage);

            if (convertedImage != null)
                buildHistograms("Converted histograms", convertedImage);
        });
    }

    private static void setLockOtherMenu(Boolean lock) {
        for (JMenuItem otherJMenuItem : otherJMenuItems)
            otherJMenuItem.setEnabled(!lock);
    }

    private static void loadImage() {
        FileDialog dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setVisible(true);

        try {
            sourceImage = Imgcodecs.imread(dialog.getDirectory() + dialog.getFile());
            setImage(sourceImage);
            setLockOtherMenu(false);

            imageName = dialog.getFile();

            convertedImage = null;
        } catch (CvException ignored) {
        }
    }

    private static void setImage(Mat image) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());
        ic = new ImageIcon(ic.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
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

    private static void exit() {
        boolean f = new File(interimPath).delete();
        if (f) System.exit(0);
        System.exit(0);
    }

    // TODO: preparing, gauss, canny and fucking masks!!!
    private static void prepare() {

    }

    private static void toGauss() {

    }

    private static void toCanny() {
        Mat img = new Mat(sourceImage.size(), sourceImage.type());
        Imgproc.Canny(sourceImage, img, 2, 2);

        convertedImage = initialiseNewMat(img);

        setImage(convertedImage);
    }

    private static Mat initialiseNewMat(Mat mat) {
        Imgcodecs.imwrite(interimPath, mat);
        return Imgcodecs.imread(interimPath);
    }

    private static void buildHistograms(String title, Mat image) {
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

        showHistogramWindow(title, histMatBitmap);
    }

    private static void showHistogramWindow(String title, Mat mat) {
        int width = 800, height = 500;
        int imageWidth = 700, imageHeight = 400;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle(title);
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
