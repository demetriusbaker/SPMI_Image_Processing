package com.company.useful_tools;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;

public class Histograms {
    public static void build(Mat image, String imageName, String extension) {
        String path = "src\\com\\company\\image.jpg";
        Imgcodecs.imwrite(path, image);
        Mat mat = Imgcodecs.imread(path);

        Mat mGray = new Mat();
        Imgproc.cvtColor(mat, mGray, Imgproc.COLOR_RGB2GRAY);
        double[] arrG;
        double[] mass = new double[500];
        for (int i = 0; i <= 255; i++) {
            mass[i] = 0;
        }
        double sum = 0;
        for (int i = 0; i < mGray.rows(); i++) {
            for (int j = 0; j < mGray.cols(); j++) {
                arrG = mGray.get(i, j);

                mass[(int) arrG[0]]++;
                sum = sum + arrG[0];
            }
        }
        double max = mass[0];
        for (int i = 0; i <= 255; i++) {
            if (max < mass[i]) max = mass[i];
        }
        Mat img1 = new Mat(600, 600, CvType.CV_8UC3, new Scalar(255, 255, 255));
        for (int i = 0; i <= 256; i++) {
            Imgproc.line(
                    img1,
                    new Point(i + 10, 600),
                    new Point(i + 10, 600 - mass[i] / max * 550),
                    new Scalar(0),
                    1,
                    Imgproc.LINE_AA,
                    0
            );
        }

        show(img1, imageName, extension);
    }

    private static void show(Mat mat, String imageName, String extension) {
        int width = 1000, height = 600;
        int imageWidth = 900, imageHeight = 500;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setTitle("Histograms");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JMenuBar mb = new JMenuBar() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = Math.max(d.width, 30);
                d.height = Math.max(d.height, 30);
                return d;
            }
        };
        mb.setVisible(true);
        frame.setJMenuBar(mb);
        JMenu jMenu = new JMenu("Image");
        JMenuItem jMenuItem = new JMenuItem("Save");
        mb.add(jMenu);
        jMenu.add(jMenuItem);
        jMenuItem.addActionListener(e -> {
            try {
                FileDialog dialog = new FileDialog(frame, "Save as", FileDialog.SAVE);
                dialog.setFile("histograms_" + imageName);
                dialog.setVisible(true);

                String savedDirectory = dialog.getDirectory() + dialog.getFile();
                Imgcodecs.imwrite(savedDirectory, mat);
            } catch (Exception ignored) {
            }
        });

        JLabel label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        frame.getContentPane().add(label);

        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(extension, mat, buf);

        int realWidth = mat.width(), realHeight = mat.height();
        while (realWidth > imageWidth || realHeight > imageHeight) {
            realWidth /= 2;
            realHeight /= 2;
        }

        ImageIcon ic = new ImageIcon(buf.toArray());
        ic = new ImageIcon(ic.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));

        label.setIcon(ic);

        frame.getContentPane().add(label);
        frame.pack();
    }
}
