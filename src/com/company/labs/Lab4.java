package com.company.labs;

import com.company.useful_tools.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;

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

    private static final Message message = new Message();

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
        JMenuItem iPreparation = new JMenuItem("Prepare like I");
        JMenuItem jPreparation = new JMenuItem("Prepare like J");
        JMenuItem gauss = new JMenuItem("Gauss");
        JMenuItem canny = new JMenuItem("Canny");

        JMenuItem sourceHistograms = new JMenuItem("Build source histograms");
        JMenuItem convertedHistograms = new JMenuItem("Build converted histograms");

        JMenu parametersMenu = new JMenu("Parameters");
        JMenuItem menuShowParameters = new JMenuItem("Show parameter values");
        JMenuItem menuSize = new JMenuItem("Change nuclear size");
        JMenuItem menuSigma = new JMenuItem("Change sigmaX");
        JMenuItem menuThreshold1 = new JMenuItem("Change threshold 1");
        JMenuItem menuThreshold2 = new JMenuItem("Change threshold 2");
        JMenuItem menuApertureSize = new JMenuItem("Change aperture size");
        JMenuItem menuTeeth = new JMenuItem("Change teeth count");

        JMenuItem[] otherJMenuItems = new JMenuItem[]{
                resetImage,
                saveImage,
                realiseMenu,
                histogramsMenu,
                parametersMenu
        };

        mb.add(menu);
        mb.add(realiseMenu);
        mb.add(histogramsMenu);
        mb.add(parametersMenu);

        menu.add(loadImage);
        menu.add(resetImage);
        menu.add(saveImage);
        menu.add(exit);

        JMenuItem[] preparations = {
                aPreparation,
                bPreparation,
                cPreparation,
                dPreparation,
                ePreparation,
                fPreparation,
                gPreparation,
                hPreparation,
                iPreparation,
                jPreparation
        };

        realiseMenu.add(preparation);
        for (JMenuItem item : preparations) preparation.add(item);
        realiseMenu.add(gauss);
        realiseMenu.add(canny);

        histogramsMenu.add(sourceHistograms);
        histogramsMenu.add(convertedHistograms);

        parametersMenu.add(menuShowParameters);
        parametersMenu.add(menuSize);
        parametersMenu.add(menuSigma);
        parametersMenu.add(menuThreshold1);
        parametersMenu.add(menuThreshold2);
        parametersMenu.add(menuApertureSize);
        parametersMenu.add(menuTeeth);

        tools = new Tools(
                frame,
                label,
                otherJMenuItems,
                extension,
                imageWidth,
                imageHeight
        );

        Parameters p = new Parameters(tools);
        tools.setLockOtherMenu(true);

        loadImage.addActionListener(e -> loadImage());
        resetImage.addActionListener(e -> resetImage());
        saveImage.addActionListener(e -> saveImage());
        exit.addActionListener(e -> System.exit(0));

        for (int i = 0; i < preparations.length; i++) {
            int code = i + 1;
            preparations[i].addActionListener(e -> {
                try {
                    convertedImage = Realisation.prepare(sourceImage, code, p.teeth, p.threshold1, p.threshold2);
                    tools.setImage(convertedImage, false);
                } catch (Exception exception) {
                    message.getParametersErrorMessage(p, true);
                }
            });
        }
        gauss.addActionListener(e -> {
            try {
                convertedImage = Realisation.toGauss(sourceImage, p.size, p.sigmaX);
                tools.setImage(convertedImage, false);
            } catch (Exception exception) {
                message.getParametersErrorMessage(p, true);
            }
        });
        canny.addActionListener(e -> {
            try {
                convertedImage = Realisation.toCanny(sourceImage, p.size, p.threshold1, p.threshold2, p.apertureSize);
                tools.setImage(convertedImage, false);
            } catch (Exception exception) {
                message.getParametersErrorMessage(p, true);
            }
        });

        sourceHistograms.addActionListener(e -> Histograms.build(sourceImage, imageName, extension));
        convertedHistograms.addActionListener(e -> {
            try {
                Histograms.build(convertedImage, imageName, extension);
            } catch (NullPointerException exception) {
                message.getNoneExistMessage(true);
            } catch (Exception exception) {
                message.getParametersErrorMessage(p, true);
            }
        });

        menuShowParameters.addActionListener(e -> p.showParameters());
        menuSize.addActionListener(e -> p.setSize());
        menuSigma.addActionListener(e -> p.setSigmaX());
        menuThreshold1.addActionListener(e -> p.setThreshold1());
        menuThreshold2.addActionListener(e -> p.setThreshold2());
        menuApertureSize.addActionListener(e -> p.setApertureSize());
        menuTeeth.addActionListener(e -> p.setTooth());
    }

    private static void loadImage() {
        FileDialog dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setVisible(true);

        String directory = dialog.getDirectory();
        String file = dialog.getFile();
        String fileName = directory + file;

        if (directory == null || file == null) {
            message.getNotChosenMessage(true);
            return;
        }
        if (!file.contains(extension)) {
            message.getIncorrectExtensionMessage(true);
            return;
        }
        if (!(tools.isValidISOLatin1(fileName))) {
            message.getNotValidNameMessage(true);
            return;
        }

        try {
            sourceImage = Imgcodecs.imread(fileName);
            tools.resized = false;
            tools.setImage(sourceImage, true);
            tools.setLockOtherMenu(false);

            imageName = dialog.getFile();

            convertedImage = null;
        } catch (Exception exception) {
            message.getNoneExistMessage(true);
        }
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
}
