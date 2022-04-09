package laboratory.lab4;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;

public class ImageProcessing extends JFrame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Version - " + Core.VERSION);
    }

    private static final int width = 800, height = 600;
    private static final int imageWidth = 600, imageHeight = 500;

    private static String imageName;
    private static Mat sourceImage;

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
        JMenu histogramsMenu = new JMenu("Build Histograms");

        JMenuItem loadImage = new JMenuItem("Load image");
        JMenuItem saveImage = new JMenuItem("Save image");
        JMenuItem exit = new JMenuItem("Exit");

        JMenuItem preparation = new JMenuItem("Preparation");
        JMenuItem gauss = new JMenuItem("Gauss");
        JMenuItem canny = new JMenuItem("Canny");

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

        setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage());
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> System.exit(0));

        preparation.addActionListener(e -> prepare());
        gauss.addActionListener(e -> toGauss());
        canny.addActionListener(e -> toCanny());

        histogramsMenu.addActionListener(e -> buildHistograms());
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
            setImage();
            setLockOtherMenu(false);

            imageName = dialog.getFile();
        } catch (CvException ignored) {
        }
    }

    private static void setImage() {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", sourceImage, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());
        ic = new ImageIcon(ic.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }

    private static void saveImage() {
        if (sourceImage == null) return;

        try {
            FileDialog dialog = new FileDialog(frame, "Save as", FileDialog.SAVE);
            dialog.setFile(imageName);
            dialog.setVisible(true);

            String savedDirectory = dialog.getDirectory() + dialog.getFile();
            Imgcodecs.imwrite(savedDirectory, sourceImage);
        } catch (Exception ignored) {
        }
    }

    private static void prepare() {

    }

    private static void toGauss() {
//        Mat img = new Mat(sourceImage.size(), sourceImage.type());
//        Imgproc.GaussianBlur(img, sourceImage, new Size(15, 15), 0);
//
//        sourceImage = img;
//        setImage();
    }

    private static void toCanny() {
        Mat img = new Mat(sourceImage.size(), sourceImage.type());
        Imgproc.Canny(sourceImage, img, 2, 2);

        sourceImage = img;
        setImage();
    }

    private static void buildHistograms() {

    }
}
