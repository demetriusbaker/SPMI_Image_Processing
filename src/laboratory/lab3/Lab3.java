package laboratory.lab3;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Lab3 extends Frame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String interimPath = "src/laboratory/lab4/image.jpg";

    private static final int width = 800, height = 600;
    private static final int imageWidth = 600, imageHeight = 500;

    private static final Mat[] channelImages = new Mat[3];

    private static String imageName;
    private static Mat sourceImage, convertedImage;

    private static JFrame frame;
    private static JLabel label;

    private static JMenuItem[] otherJMenuItems;

    private static Affine transformer;

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
        JMenu affineMenu = new JMenu("Affine transformation");
        JMenu colorMenu = new JMenu("Color");

        JMenuItem loadImage = new JMenuItem("Load image");
        JMenuItem saveImage = new JMenuItem("Save image");
        JMenuItem exit = new JMenuItem("Exit");

        JMenuItem move = new JMenuItem("Move");
        JMenuItem scale = new JMenuItem("Scale");

        JMenu turn = new JMenu("Turning");
        JMenuItem plusOne = new JMenuItem("+1");
        JMenuItem zero = new JMenuItem("0");
        JMenuItem minusOne = new JMenuItem("-1");

        JMenuItem shift = new JMenuItem("Shift");

        JMenuItem grayImage = new JMenuItem("Convert to gray");
        JMenuItem channels = new JMenuItem("Build RGB channels");
        JMenuItem everyChannel = new JMenuItem("Save every channel");

        otherJMenuItems = new JMenuItem[]{
                saveImage,
                affineMenu,
                colorMenu
        };

        mb.add(menu);
        mb.add(affineMenu);
        mb.add(colorMenu);

        menu.add(loadImage);
        menu.add(saveImage);
        menu.add(exit);

        affineMenu.add(move);
        affineMenu.add(scale);
        affineMenu.add(turn);
        affineMenu.add(shift);

        turn.add(plusOne);
        turn.add(zero);
        turn.add(minusOne);

        colorMenu.add(grayImage);
        colorMenu.add(channels);
        colorMenu.add(everyChannel);

        setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage(everyChannel));
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> exit());

        move.addActionListener(e -> setMoving());
        scale.addActionListener(e -> setScaling());

        plusOne.addActionListener(e -> setTurning(1));
        zero.addActionListener(e -> setTurning(0));
        minusOne.addActionListener(e -> setTurning(-1));

        shift.addActionListener(e -> setShifting());

        grayImage.addActionListener(e -> convertRGB2GRAY());
        channels.addActionListener(e -> buildChannels(everyChannel));
        everyChannel.addActionListener(e -> saveEveryChannel());
    }

    // First menu

    private static void loadImage(JMenuItem everyChannel) {
        FileDialog dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setVisible(true);

        try {
            sourceImage = Imgcodecs.imread(dialog.getDirectory() + dialog.getFile());
            setImage(sourceImage);
            setLockOtherMenu(false);

            imageName = dialog.getFile();

            convertedImage = null;
            transformer = new Affine(sourceImage);
            everyChannel.setEnabled(false);
        } catch (CvException ignored) {
        }
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

    private static void setMoving() {
        JFrame jFrame = new JFrame("Move");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(500, 200));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        JSlider slider = new JSlider(0, 100, 1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 1 to 100", SwingConstants.CENTER));
        northPanel.add(slider);
        jFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            int movingNumber = slider.getValue();
            convertedImage = initialiseNewMat(transformer.move(movingNumber));
            setImage(convertedImage);
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private static void setScaling() {
        JFrame jFrame = new JFrame("Scale");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(500, 200));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        JSlider slider = new JSlider(200, 2000, 200);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(200);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        String text = "Set from 200 to 2000 (default: 1000)";
        northPanel.add(new JLabel(text, SwingConstants.CENTER));
        northPanel.add(slider);
        jFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            double scale = slider.getValue() / 1000.0;
            convertedImage = initialiseNewMat(transformer.scale(scale));
            setImage(convertedImage, (int) (imageWidth * scale), (int) (imageHeight * scale));
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private static void setTurning(int flipCode) {
        convertedImage = initialiseNewMat(transformer.turn(flipCode));
        setImage(convertedImage);
    }

    private static void setShifting() {
        JFrame jFrame = new JFrame("Shift");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(250, 150));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText("0.6|0.5|0.6|0.4|0.4|0.3");
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        String text = "Set pattern like: 0.6|0.5|0.6|0.4|0.4|0.3";
        northPanel.add(new JLabel(text, SwingConstants.CENTER));
        northPanel.add(textField);
        jFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> {
            try {
                String[] slice = textField.getText().split("\\|");
                Double[] arr = new Double[6];
                for (int i = 0; i < arr.length; i++)
                    arr[i] = Double.parseDouble(slice[i]);
                convertedImage = initialiseNewMat(transformer.shift(arr));
                setImage(convertedImage);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        null,
                        "Incorrect pattern for shifting!!!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        southPanel.add(button);
        jFrame.add(southPanel, BorderLayout.SOUTH);
    }

    // Third menu

    private static void convertRGB2GRAY() {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);

        convertedImage = initialiseNewMat(grayImage);

        setImage(convertedImage);
    }

    private static void buildChannels(JMenuItem everyChannel) {
        for (int index = 0; index < channelImages.length; index++) {
            int red = 255, green = 255, blue = 255;
            String channelName;

            if (index == 0) {
                blue = 0;
                channelName = "Red channel";
            } else if (index == 1) {
                green = 0;
                channelName = "Green channel";
            } else {
                red = 0;
                channelName = "Blue channel";
            }

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

            channelImages[index] = mat;
            showChannels(channelName, mat);
        }

        everyChannel.setEnabled(true);
    }

    private static void saveEveryChannel() {
        String[] colorName = new String[]{"_red.jpg", "_green.jpg", "_blue.jpg"};
        for (int i = 0; i < channelImages.length; i++) {
            try {
                FileDialog dialog = new FileDialog(frame, "Save as", FileDialog.SAVE);
                dialog.setFile(imageName.replace(".jpg", colorName[i]));
                dialog.setVisible(true);

                String savedDirectory = dialog.getDirectory() + dialog.getFile();
                Imgcodecs.imwrite(savedDirectory, channelImages[i]);
            } catch (Exception ignored) {
            }
        }
    }

    // Tools methods

    private static void setLockOtherMenu(Boolean lock) {
        for (JMenuItem otherJMenuItem : otherJMenuItems)
            otherJMenuItem.setEnabled(!lock);
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

    private static void setImage(Mat image, int width, int height) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());
        ic = new ImageIcon(ic.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }

    private static Mat initialiseNewMat(Mat mat) {
        Imgcodecs.imwrite(interimPath, mat);
        return Imgcodecs.imread(interimPath);
    }

    private static void showChannels(String title, Mat mat) {
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