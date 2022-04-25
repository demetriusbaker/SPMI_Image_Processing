package laboratory.lab3;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Hashtable;

public class Lab3 extends Frame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String interimPath = "src/laboratory/buffer.jpg";

    private static final int width = 1000, height = 800;
    private static final int imageWidth = 600, imageHeight = 500;

    private static String imageName;
    private static Mat sourceImage, convertedImage;

    private static JFrame frame;
    private static JLabel label;

    private static JMenuItem[] otherJMenuItems;

    private static Affine transformer;

    private static boolean resized;

    private static final String extension = ".jpg";

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle("Gay party window");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        label = new JLabel();
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
        JMenuItem loadImage = new JMenuItem("Load image");
        JMenuItem reset = new JMenuItem("Reset image");
        JMenuItem saveImage = new JMenuItem("Save image");
        JMenuItem exit = new JMenuItem("Exit from this window");

        JMenu affineMenu = new JMenu("Affine transformation");
        JMenuItem move = new JMenuItem("Move");
        JMenuItem scale = new JMenuItem("Scale");
        JMenuItem turn = new JMenuItem("Turning");
        JMenuItem shift = new JMenuItem("Shift");

        JMenu colorMenu = new JMenu("Color filter");
        JMenuItem grayImage = new JMenuItem("2GRAY");
        JMenuItem red = new JMenuItem("Red channel");
        JMenuItem green = new JMenuItem("Green channel");
        JMenuItem blue = new JMenuItem("Blue channel");

        otherJMenuItems = new JMenuItem[]{
                reset,
                saveImage,
                affineMenu,
                colorMenu
        };

        mb.add(menu);
        mb.add(affineMenu);
        mb.add(colorMenu);

        menu.add(loadImage);
        menu.add(reset);
        menu.add(saveImage);
        menu.add(exit);

        affineMenu.add(move);
        affineMenu.add(scale);
        affineMenu.add(turn);
        affineMenu.add(shift);

        colorMenu.add(grayImage);
        colorMenu.add(red);
        colorMenu.add(green);
        colorMenu.add(blue);

        setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage());
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> exit());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        move.addActionListener(e -> move());
        scale.addActionListener(e -> setScaling());
        turn.addActionListener(e -> setTurning());
        shift.addActionListener(e -> setShifting());

        grayImage.addActionListener(e -> convertRGB2GRAY());
        red.addActionListener(e -> buildColorChannel(1));
        green.addActionListener(e -> buildColorChannel(2));
        blue.addActionListener(e -> buildColorChannel(3));
        reset.addActionListener(e -> resetImage());
    }

    // First menu

    private static void loadImage() {
        FileDialog dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setVisible(true);

        String directory = dialog.getDirectory();
        String file = dialog.getFile();
        String fileName = directory + file;

        if (directory == null || file == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "You didn't choose anything jpeg file!\n",
                    "Choose jpeg image",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        if (!file.contains(extension)) {
            JOptionPane.showMessageDialog(
                    null,
                    "The file of wrong extension!\n" +
                            "Check please extension!",
                    "Extension error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (!isValidISOLatin1(fileName)) {
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
        resized = false;
        setImage(sourceImage, true);
        setLockOtherMenu(false);

        imageName = dialog.getFile();

        convertedImage = null;
        transformer = new Affine(sourceImage);
    }

    private static void resetImage() {
        convertedImage = sourceImage;
        setImage(convertedImage, false);
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

    // Second menu

    private static void move() {
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int step = 5;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> moveImage(step, 0);
                    case KeyEvent.VK_DOWN -> moveImage(-step, 0);
                    case KeyEvent.VK_LEFT -> moveImage(0, -step);
                    case KeyEvent.VK_RIGHT -> moveImage(0, step);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            private static void moveImage(int up, int right) {
                int width = frame.getWidth();
                int height = frame.getHeight();
                Point point = label.getLocation();
                int pointX = point.x + right;
                int pointY = point.y - up;
                Point newPoint = new Point(pointX, pointY);
                System.out.println(newPoint);
                System.out.println(width);
                System.out.println(height);
                label.setLocation(newPoint);
            }
        });
    }

//    private static void setMoving() {
//        JFrame jFrame = createNewChoiceWindow("Moving", 500, 200);
//
//        JSlider slider = new JSlider(-100, 100, 1);
//        slider.setValue(0);
//        slider.setPaintTicks(true);
//        slider.setPaintLabels(true);
//        slider.setPaintTrack(true);
//        slider.setMinorTickSpacing(5);
//        slider.setMajorTickSpacing(10);
//
//        JPanel northPanel = new JPanel();
//        northPanel.setLayout(new GridLayout(2, 2));
//        northPanel.add(new JLabel("Set from 0 to 100", SwingConstants.CENTER));
//        northPanel.add(slider);
//        jFrame.add(northPanel, BorderLayout.NORTH);
//
//        JPanel southPanel = new JPanel();
//        JButton button = new JButton("Confirm");
//        button.addActionListener(e -> {
//            int movingNumber = slider.getValue();
//            convertedImage = initialiseNewMat(transformer.move(movingNumber));
//            double scale = convertedImage.size().width / sourceImage.size().width;
//            setAffineImage(convertedImage, scale);
//        });
//        southPanel.add(button);
//        jFrame.add(southPanel, BorderLayout.SOUTH);
//    }

    private static void setScaling() {
        JFrame jFrame = createNewChoiceWindow("Scaling", 500, 250);

        JSlider slider = new JSlider(400, 1400);
        slider.setValue(1000);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
        int startI = 400;
        double startD = 0.4;
        while (startI <= 1400) {
            String scale = new DecimalFormat("#0.0").format(startD);
            hashtable.put(startI, new JLabel(scale));
            startI += 100;
            startD += 0.1;
        }
        slider.setLabelTable(hashtable);
        slider.setPaintLabels(true);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        String text = "Set from 0.4 to 1.4 (default: 1.0)";
        northPanel.add(new JLabel(text, SwingConstants.CENTER));
        northPanel.add(slider);
        jFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            double scale = slider.getValue() / 1000.0;
            convertedImage = initialiseNewMat(transformer.scale(scale));
            setAffineImage(convertedImage, scale);
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private static void setTurning() {
        JFrame jFrame = createNewChoiceWindow("Scaling", 500, 200);

        JSlider slider = new JSlider(0, 360, 1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(15);
        slider.setMajorTickSpacing(30);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 0 to 360°", SwingConstants.CENTER));
        northPanel.add(slider);
        jFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            int movingNumber = slider.getValue();
            convertedImage = initialiseNewMat(transformer.turn(movingNumber));
            double scaleW = convertedImage.size().width / sourceImage.size().width;
            double scaleH = convertedImage.size().height / sourceImage.size().height;
            setAffineImage(convertedImage, Math.min(scaleH, scaleW));
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private static void setShifting() {
        JFrame jFrame = createNewChoiceWindow("Shifting", 400, 200);

        Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
        hashtable.put(0, new JLabel("0x"));
        hashtable.put(2, new JLabel("0.2x"));
        hashtable.put(4, new JLabel("0.4x"));
        hashtable.put(6, new JLabel("0.6x"));
        hashtable.put(8, new JLabel("0.8x"));
        hashtable.put(10, new JLabel("1.0x"));

        JSlider slider = new JSlider(0, 10, 1);
        slider.setValue(0);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(2);
        slider.setLabelTable(hashtable);

        JPanel northPanel = new JPanel();
        northPanel.add(slider);
        jFrame.add(northPanel, BorderLayout.NORTH);

        hashtable = new Hashtable<>();
        hashtable.put(0, new JLabel("0y"));
        hashtable.put(2, new JLabel("0.2y"));
        hashtable.put(4, new JLabel("0.4y"));
        hashtable.put(6, new JLabel("0.6y"));
        hashtable.put(8, new JLabel("0.8y"));
        hashtable.put(10, new JLabel("1.0y"));

        JSlider slider1 = new JSlider(0, 10, 1);
        slider1.setValue(0);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);
        slider1.setPaintTrack(true);
        slider1.setMinorTickSpacing(1);
        slider1.setMajorTickSpacing(2);
        slider1.setLabelTable(hashtable);

        JPanel middlePanel = new JPanel();
        middlePanel.add(slider1);
        jFrame.add(middlePanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            double axisX = slider.getValue() / 10.0;
            double axisY = slider1.getValue() / 10.0;
            convertedImage = initialiseNewMat(transformer.shift(axisX, axisY));
            setAffineImage(convertedImage, 1.0);
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    // Third menu

    private static void convertRGB2GRAY() {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);

        convertedImage = initialiseNewMat(grayImage);

        setImage(convertedImage, false);
    }

    private static void buildColorChannel(int color) {
        int red = 255, green = 255, blue = 255;

        if (color == 1) blue = 0;
        else if (color == 2) green = 0;
        else red = 0;

        Mat mat = new Mat(sourceImage.size(), sourceImage.type());
        int channels = sourceImage.channels();
        double[] pixel;
        for (int i = 0, r_length = sourceImage.rows(); i < r_length; i++) {
            for (int j = 0, c_length = sourceImage.cols(); j < c_length; j++) {
                if (channels == 3) {
                    pixel = sourceImage.get(i, j).clone();
                    pixel[0] -= red;
                    pixel[1] -= green;
                    pixel[2] -= blue;
                    mat.put(i, j, pixel);
                } else {
                    mat.put(i, j, sourceImage.get(i, j).clone());
                }
            }
        }

        convertedImage = initialiseNewMat(mat);
        setImage(convertedImage, false);
    }

    // Tools methods

    private static void setLockOtherMenu(Boolean lock) {
        for (JMenuItem otherJMenuItem : otherJMenuItems)
            otherJMenuItem.setEnabled(!lock);
    }

    private static void setImage(Mat image, boolean downloadMode) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(extension, image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());

        if (downloadMode || resized) {
            int realWidth = image.width(), realHeight = image.height();
            while (realWidth > imageWidth || realHeight > imageHeight) {
                realWidth /= 2;
                realHeight /= 2;
                resized = true;
            }

            ic = new ImageIcon(ic.getImage().getScaledInstance(realWidth, realHeight, Image.SCALE_DEFAULT));
        } else
            ic = new ImageIcon(ic.getImage().getScaledInstance(image.width(), image.height(), Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }

    private static void setAffineImage(Mat image, double scale) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(extension, image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());

        if (resized) {
            int realWidth = image.width(), realHeight = image.height();
            while (realWidth > imageWidth * scale || realHeight > imageHeight * scale) {
                realWidth /= 2;
                realHeight /= 2;
                resized = true;
            }

            ic = new ImageIcon(ic.getImage().getScaledInstance(realWidth, realHeight, Image.SCALE_DEFAULT));
        } else
            ic = new ImageIcon(ic.getImage().getScaledInstance(image.width(), image.height(), Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }

    private static Mat initialiseNewMat(Mat mat) {
        Imgcodecs.imwrite(interimPath, mat);
        return Imgcodecs.imread(interimPath);
    }

    private static boolean isValidISOLatin1(String s) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(s);
    }

    private static JFrame createNewChoiceWindow(String name, int width, int height) {
        JFrame jFrame = new JFrame(name);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(width, height));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        return jFrame;
    }
}
