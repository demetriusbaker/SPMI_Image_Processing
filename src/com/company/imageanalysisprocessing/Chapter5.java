package com.company.imageanalysisprocessing;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;

public class Chapter5 {
    public static String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\com\\company\\mouse.jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat m = new Mat(1, 1, CvType.CV_8UC4, new Scalar(0, 128, 200, 218));
        Mat alpha = new Mat(m.rows(), m.cols(), CvType.CV_8UC1);
        ArrayList<Mat> listSrc = new ArrayList<>();
        listSrc.add(m);
        ArrayList<Mat> listDst = new ArrayList<>();

        listDst.add(alpha);
        Core.mixChannels(listSrc, listDst, new MatOfInt(3, 0));
        System.out.println(alpha.dump()); // [218]
        System.out.println(alpha);
        // Mat [ 1*1*CV_8UC1, isCont=true, isSubmat=false,
        // nativeObj=0x1a616100, dataAddr=0x11cb580 ]
        alpha.put(0, 0, 111);
        listSrc.clear();
        listSrc.add(alpha);
        listDst.clear();
        listDst.add(m);
        Core.mixChannels(listSrc, listDst, new MatOfInt(0, 3));
        System.out.println(m.dump()); // [ 0, 128, 200, 111]

        m = new Mat(1, 3, CvType.CV_8UC3);
        m.put(0, 0,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        );
        Mat ch = new Mat();
        // Извлечение значений каналов
        Core.extractChannel(m, ch, 0);
        System.out.println(m.dump());
        // [ 1, 2, 3, 4, 5, 6, 7, 8, 9]
        System.out.println(ch.dump()); // [ 1, 4, 7]
        Core.extractChannel(m, ch, 1);
        System.out.println(ch.dump()); // [ 2, 5, 8]
        Core.extractChannel(m, ch, 2);
        System.out.println(ch.dump()); // [ 3, 6, 9]
        // Вставка значений в канал с индексом 1
        Core.insertChannel(ch, m, 1);
        System.out.println(m.dump());
        // [ 1, 3, 3, 4, 6, 6, 7, 9, 9]

        Mat img = Imgcodecs.imread(path);
        Mat img2 = new Mat();
        Core.flip(img, img2, 0);
        img.release();
        img2.release();
    }
}
