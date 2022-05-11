package com.company.useful_tools;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Realisation {
    public static Mat prepare(Mat mat, int code, int teeth, int t1, int t2) {
        Mat image = mat.clone();
        Imgproc.cvtColor(mat, image, Imgproc.COLOR_RGB2GRAY);

        switch (code) {
            case 1 -> one(image, t1);
            case 2 -> two(image, t1, t2);
            case 3 -> three(image, t1, t2);
            case 4 -> four(image, t1);
            case 5 -> five(image);
            case 6 -> six(image);
            case 7 -> seven(image, t1, t2);
            case 8 -> eight(image, t1, t2);
            case 9 -> nine(image, t1, t2);
            default -> {
                if (teeth >= 2) ten(image, teeth);
                else five(image);
            }
        }

        return image;
    }

    public static Mat toGauss(Mat mat, int size, int sigmaX) {
        Mat newMat = new Mat(mat.rows(), mat.cols(), mat.type());
        Imgproc.GaussianBlur(mat, newMat, new Size(size, size), sigmaX);
        return newMat;
    }

    public static Mat toCanny(Mat sourceImage, int size, int threshold1, int threshold2) {
        Mat grayImage = new Mat(), detectedEdges = new Mat();
        Imgproc.cvtColor(sourceImage, grayImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(grayImage, detectedEdges, new Size(size, size));
        Imgproc.Canny(detectedEdges, detectedEdges, threshold1, threshold2);
        return detectedEdges;
    }

    private static void one(Mat image, int t1) {
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

    private static void two(Mat image, int t1, int t2) {
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

    private static void three(Mat image, int t1, int t2) {
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

    private static void four(Mat image, int t1) {
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

    private static void five(Mat image) {
        int min = (int) findMin(image), max = (int) findMax(image);
        for (int i = 0; i < image.rows(); i++)
            for (int j = 0; j < image.cols(); j++) {
                double[] data = image.get(i, j);

                for (int p = 0; p < image.channels(); p++)
                    data[p] = (int) (255 * (data[p] - min) / (max - min));

                image.put(i, j, data);
            }
    }

    private static void six(Mat image) {
        for (int i = 0; i < image.rows(); i++)
            for (int j = 0; j < image.cols(); j++) {
                double[] data = image.get(i, j);

                for (int p = 0; p < image.channels(); p++)
                    data[p] = 255 - data[p];

                image.put(i, j, data);
            }
    }

    private static void seven(Mat image, int t1, int t2) {
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

    private static void eight(Mat image, int t1, int t2) {
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

    private static void nine(Mat image, int t1, int t2) {
        for (int i = 0; i < image.rows(); i++)
            for (int j = 0; j < image.cols(); j++) {
                double[] data = image.get(i, j);

                for (int p = 0; p < image.channels(); p++) {
                    int cell = (int) (255 * (data[p] - (double) t1) / ((double) t2 - (double) t1));
                    if ((double) t2 >= data[p] && data[p] >= (double) t1) data[p] = cell;
                    else data[p] = data[p];
                }

                image.put(i, j, data);
            }
    }

    private static void ten(Mat image, int teeth) {
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

    private static double findMin(Mat image) {
        double min = 0;
        for (int i = 0; i < image.rows(); i++)
            for (int j = 0; j < image.cols(); j++) {
                double[] data = image.get(i, j);

                for (int p = 0; p < image.channels(); p++)
                    if (data[p] < min)
                        min = data[p];
            }
        return min;
    }

    private static double findMax(Mat image) {
        double max = 0;
        for (int i = 0; i < image.rows(); i++)
            for (int j = 0; j < image.cols(); j++) {
                double[] data = image.get(i, j);

                for (int p = 0; p < image.channels(); p++)
                    if (data[p] > max)
                        max = data[p];
            }
        return max;
    }
}
