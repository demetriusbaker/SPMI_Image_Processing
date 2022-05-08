package com.company.useful_tools;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Realisation {
    public static Mat prepare(Mat mat, int code, int teeth, int t1, int t2) {
        Mat image = mat.clone();
        Imgproc.cvtColor(mat, image, Imgproc.COLOR_RGB2GRAY);

        switch (code) {
            case 1 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            if (data[p] >= t1) data[p] = 255;
                            else data[p] = 0;
                        }

                        image.put(i, j, data);
                    }
            }
            case 2 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            if (t2 >= data[p] && data[p] >= t1) data[p] = 255;
                            else data[p] = 0;
                        }

                        image.put(i, j, data);
                    }
            }
            case 3 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            if (t2 >= data[p] && data[p] >= t1) data[p] = 255;
                            else data[p] = data[p];
                        }

                        image.put(i, j, data);
                    }
            }
            case 4 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            if (data[p] >= t1) data[p] = 255;
                            else data[p] = data[p];
                        }

                        image.put(i, j, data);
                    }
            }
            case 5 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++)
                            data[p] = (int) (255 * (data[p] - t1) / (t2 - t1));

                        image.put(i, j, data);
                    }
            }
            case 6 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++)
                            data[p] = 255 - data[p];

                        image.put(i, j, data);
                    }
            }
            case 7 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            int cell = (int) (255 * (data[p] - (double) t1) / ((double) t2 - (double) t1));
                            if ((double) t2 >= data[p] && data[p] >= (double) t1) data[p] = cell;
                            else data[p] = 0;
                        }

                        image.put(i, j, data);
                    }
            }
            case 8 -> {
                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            int cell = (int) (255 * (data[p] - (double) t1) / ((double) t2 - (double) t1));
                            if ((double) t2 >= data[p] && data[p] >= (double) t1) data[p] = cell;
                            else data[p] = 255;
                        }

                        image.put(i, j, data);
                    }
            }
            case 9 -> {
                int average = (int) (((double) t1 + (double) t2) / 2);

                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            int cell = (int) (255 * (data[p] - (double) t1) / ((double) t2 - (double) t1));
                            if ((double) t2 >= data[p] && data[p] >= (double) t1) data[p] = cell;
                            else data[p] = average;
                        }

                        image.put(i, j, data);
                    }
            }
            default -> {
                int step = 255 / teeth;

                for (int i = 0; i < image.rows(); i++)
                    for (int j = 0; j < image.cols(); j++) {
                        double[] data = image.get(i, j);

                        for (int p = 0; p < image.channels(); p++) {
                            int m = (int) (data[p] / step);
                            int left = step * m;
                            int right;
                            if (step * (m + 1) == 255) right = 255;
                            else right = step * (m + 1);
                            data[p] = (int) (255 * (data[p] - left) / (right - left));
                        }

                        image.put(i, j, data);
                    }
            }
        }

        return image;
    }

    public static Mat toGauss(Mat mat, int size, int sigmaX) {
        Mat newMat = new Mat(mat.rows(), mat.cols(), mat.type());
        Imgproc.GaussianBlur(mat, newMat, new Size(size, size), sigmaX);
        return newMat;
    }

    public static Mat toCanny(Mat sourceImage, int size, int threshold1, int threshold2, int apertureSize) {
        Mat grayImage = new Mat(), detectedEdges = new Mat();
        Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(grayImage, detectedEdges, new Size(size, size));
        Imgproc.Canny(detectedEdges, detectedEdges, threshold1, threshold2, apertureSize);
        return detectedEdges;
    }
}
