package com.company.useful_tools;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class Histograms {
    public static void build(Mat image, String imageName, String extension) {
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
        Scalar grayScalar = new Scalar(127, 127, 127, 127);
        MatOfInt matOfInt = new MatOfInt(0);

        // Create an array to be saved in the histogram and a second array, on which the histogram chart will be drawn.
        Mat hist = new Mat();
        Mat histMatBitmap = new Mat(imageSize, image.type());

        Imgproc.calcHist(
                Collections.singletonList(image),
                matOfInt,
                new Mat(),
                hist,
                histogramSize, histogramRange
        );
        Core.normalize(hist, hist, histogramHeight, 0, Core.NORM_INF);
        for (int j = 0; j < histSize; j++) {
            Point p1 = new Point(
                    binWidth * (j - 1),
                    histogramHeight - Math.round(hist.get(j - 1, 0)[0])
            );
            Point p2 = new Point(
                    binWidth * j,
                    histogramHeight - Math.round(hist.get(j, 0)[0])
            );
            Imgproc.line(histMatBitmap, p1, p2, grayScalar, 2, 8, 0);
        }

        for (int j = 0; j < histSize; j++) {
            Point p1 = new Point(
                    binWidth * (j - 1),
                    histogramHeight - Math.round(hist.get(j - 1, 0)[0])
            );
            Point p2 = new Point(
                    binWidth * j,
                    histogramHeight - Math.round(hist.get(j, 0)[0])
            );
            Imgproc.line(histMatBitmap, p1, p2, grayScalar, 2, 8, 0);
        }

        show(histMatBitmap, imageName, extension);
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
