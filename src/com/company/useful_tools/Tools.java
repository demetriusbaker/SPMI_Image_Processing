package com.company.useful_tools;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class Tools {
    private final JFrame jFrame;
    private final JLabel jLabel;
    private final JMenuItem[] jMenuItems;
    private final String extension;
    private final int width;
    private final int height;

    public boolean resized;

    public Tools(
            JFrame jFrame,
            JLabel jLabel,
            JMenuItem[] jMenuItems,
            String extension,
            int width,
            int height
    ) {
        this.jFrame = jFrame;
        this.jLabel = jLabel;
        this.jMenuItems = jMenuItems;
        this.extension = extension;
        this.width = width;
        this.height = height;
    }

    public void setLockOtherMenu(Boolean lock) {
        for (JMenuItem otherJMenuItem : jMenuItems)
            otherJMenuItem.setEnabled(!lock);
    }

    public void setImage(Mat image, boolean downloadMode) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(extension, image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());

        if (downloadMode || resized) {
            int realWidth = image.width(), realHeight = image.height();
            while (realWidth > width || realHeight > height) {
                realWidth /= 2;
                realHeight /= 2;
                resized = true;
            }

            ic = new ImageIcon(ic.getImage().getScaledInstance(realWidth, realHeight, Image.SCALE_DEFAULT));
        } else
            ic = new ImageIcon(ic.getImage().getScaledInstance(image.width(), image.height(), Image.SCALE_DEFAULT));

        jLabel.setIcon(ic);

        jFrame.getContentPane().add(jLabel);
        jFrame.pack();
    }

    public void setAffineImage(Mat image, double scale) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(extension, image, buf);

        ImageIcon ic = new ImageIcon(buf.toArray());

        int realWidth = image.width(), realHeight = image.height();
        while (realWidth > width * scale || realHeight > height * scale) {
            realWidth /= 2;
            realHeight /= 2;
        }

        ic = new ImageIcon(ic.getImage().getScaledInstance(realWidth, realHeight, Image.SCALE_DEFAULT));

        jLabel.setIcon(ic);

        jFrame.getContentPane().add(jLabel);
        jFrame.pack();
    }


    public boolean isValidISOLatin1(String s) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(s);
    }

    public JFrame createNewChoiceWindow(String name, int width, int height) {
        JFrame jFrame = new JFrame(name);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(width, height));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        return jFrame;
    }

    public void moveImageInsideScreen(int up, int right) {
        Point point = jLabel.getLocation();
        int pointX = point.x + right;
        int pointY = point.y - up;
        Point newPoint = new Point(pointX, pointY);

        int wBorder = (jFrame.getWidth() - jLabel.getIcon().getIconWidth()) / 2 - 100;
        int hBorder = (jFrame.getHeight() - jLabel.getIcon().getIconHeight()) / 2 - 100;

        if (wBorder > Math.abs(pointX) && hBorder > Math.abs(pointY))
            jLabel.setLocation(newPoint);
    }
}
