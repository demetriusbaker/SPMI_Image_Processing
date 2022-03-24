package com.company;

import org.opencv.core.*;

public class Chapter2 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat mat = new Mat();
        System.out.println(mat.dump());
        System.out.println(mat.cols());
        System.out.println(mat.width());
        System.out.println(mat.rows());
        System.out.println(mat.height());

        mat = new Mat(3, 2, CvType.CV_8UC1);
        System.out.println(mat.dump());
        System.out.println(mat.cols());
        System.out.println(mat.width());
        System.out.println(mat.rows());
        System.out.println(mat.height());

        mat = new Mat(3, 2, CvType.CV_8UC1, new Scalar(1.0));
        System.out.println(mat.dump());
        System.out.println(mat.cols());
        System.out.println(mat.width());
        System.out.println(mat.rows());
        System.out.println(mat.height());

        mat = new Mat(new Size(2, 3), CvType.CV_8UC3);
        System.out.println(mat.dump());

        mat = new Mat(new Size(2, 3), CvType.CV_8UC3, new Scalar(1, 2, 3));
        System.out.println(mat.dump());

        mat = Mat.ones(3, 2, CvType.CV_8UC1);
        System.out.println(mat.dump());

        mat = Mat.eye(13, 13, CvType.CV_8UC1);
        System.out.println(mat.dump());

        Core.setIdentity(mat);
        System.out.println(mat.dump());

        mat.create(2, 3, CvType.CV_8UC1);
        mat.setTo(new Scalar(1));
        System.out.println(mat.dump());

        mat.create(new Size(2, 2), CvType.CV_8UC1);
        mat.setTo(new Scalar(5));
        System.out.println(mat.dump());

        System.out.println(mat.total());

        mat = new Mat(3, 3, CvType.CV_8UC3);
        System.out.println(mat.elemSize());
        System.out.println(mat.elemSize1());

        mat = new Mat(3, 3, CvType.CV_32FC3);
        System.out.println(mat.elemSize());
        System.out.println(mat.elemSize1());

        System.out.println();

        // 8 бит (–128 ... 127)
        System.out.println(CvType.CV_8SC1); // 1
        System.out.println(CvType.CV_8SC2); // 9
        System.out.println(CvType.CV_8SC3); // 17
        System.out.println(CvType.CV_8SC4); // 25
// 8 бит (0 ... 255)
        System.out.println(CvType.CV_8UC1); // 0
        System.out.println(CvType.CV_8UC2); // 8
        System.out.println(CvType.CV_8UC3); // 16
        System.out.println(CvType.CV_8UC4); // 24
// 16 бит (–32768 ... 32767)
        System.out.println(CvType.CV_16SC1); // 3
        System.out.println(CvType.CV_16SC2); // 11
        System.out.println(CvType.CV_16SC3); // 19
        System.out.println(CvType.CV_16SC4); // 27
// 16 бит (0 ... 65535)
        System.out.println(CvType.CV_16UC1); // 2
        System.out.println(CvType.CV_16UC2); // 10
        System.out.println(CvType.CV_16UC3); // 18
        System.out.println(CvType.CV_16UC4); // 26
// 32 бита (–2 147 483 648 ... 2 147 483 647)
        System.out.println(CvType.CV_32SC1); // 4
        System.out.println(CvType.CV_32SC2); // 12
        System.out.println(CvType.CV_32SC3); // 20
        System.out.println(CvType.CV_32SC4); // 28
// 32 бита float
        System.out.println(CvType.CV_32FC1); // 5
        System.out.println(CvType.CV_32FC2); // 13
        System.out.println(CvType.CV_32FC3); // 21
        System.out.println(CvType.CV_32FC4); // 29
// 64 бита double
        System.out.println(CvType.CV_64FC1); // 6
        System.out.println(CvType.CV_64FC2); // 14
        System.out.println(CvType.CV_64FC3); // 22
        System.out.println(CvType.CV_64FC4); // 30

        mat = new Mat(1, 2, CvType.CV_8UC(7));
        System.out.println(mat.channels());
        mat = new Mat(1, 2, CvType.CV_8UC(511));
        System.out.println(mat.channels());
        System.out.println(mat.depth());

        Mat m = new Mat(3, 2, CvType.CV_8UC1);
        System.out.println(m.type()); // 0
        System.out.println(m.channels()); // 1
        System.out.println(m.depth()); // 0
        m = new Mat(3, 2, CvType.CV_8SC2);
        System.out.println(m.channels()); // 2
        System.out.println(m.depth()); // 1
        m = new Mat(3, 2, CvType.CV_16UC3);
        System.out.println(m.channels()); // 3
        System.out.println(m.depth()); // 2
        m = new Mat(3, 2, CvType.CV_16SC3);
        System.out.println(m.channels()); // 3
        System.out.println(m.depth()); // 3
        m = new Mat(3, 2, CvType.CV_32SC3);
        System.out.println(m.channels()); // 3
        System.out.println(m.depth()); // 4
        m = new Mat(3, 2, CvType.CV_32FC4);
        System.out.println(m.channels()); // 4
        System.out.println(m.depth()); // 5
        m = new Mat(3, 2, CvType.CV_64FC4);
        System.out.println(m.channels()); // 4
        System.out.println(m.depth()); // 6

        System.out.println(CvType.CV_8U); // 0
        System.out.println(CvType.CV_8S); // 1
        System.out.println(CvType.CV_16U); // 2
        System.out.println(CvType.CV_16S); // 3
        System.out.println(CvType.CV_32S); // 4
        System.out.println(CvType.CV_32F); // 5
        System.out.println(CvType.CV_64F); // 6
        System.out.println(CvType.CV_USRTYPE1); // 7

        mat = new Mat(3, 3, CvType.CV_8SC1);
        mat.diag(0).setTo(new Scalar(0));
        mat.diag(1).setTo(new Scalar(1));
        mat.diag(2).setTo(new Scalar(2));
        mat.diag(-1).setTo(new Scalar(-1));
        mat.diag(-2).setTo(new Scalar(-2));
        System.out.println(mat.dump());

        mat = new Mat(2, 3, CvType.CV_8UC1, new Scalar(1));
        System.out.println(mat.isSubmatrix());
        Mat subMat = mat.row(0);
        System.out.println(subMat.isSubmatrix());

        Mat mat1 = mat.clone();
        System.out.println(mat1.dump());

        System.out.println(mat.dump());
        System.out.println(mat.t().dump());

        mat = new Mat(10, 10, CvType.CV_8UC1, new Scalar(1));
        System.out.println(mat);
// Mat [ 10*10*CV_8UC1, isCont=true, isSubmat=false,
// nativeObj=0x19cc8d70, dataAddr=0x19cc9bc0 ]
        mat.release();
        System.out.println(mat);
// Mat [ 0*0*CV_8UC1, isCont=true, isSubmat=false,
// nativeObj=0x19cc8d70, dataAddr=0x0 ]
        mat = null;

        mat = new Mat(1000, 1000, CvType.CV_8UC4);
        long n = System.currentTimeMillis();
        for (int i = 0, r = mat.rows(); i < r; i++) {
            for (int j = 0, c = mat.cols(); j < c; j++) {
                mat.put(i, j, 10, 20, 30, 40);
            }
        }
        System.out.println("put() - " + (System.currentTimeMillis() - n));
        n = System.currentTimeMillis();
        mat.setTo(new Scalar(10, 20, 30, 40));
        System.out.println("setTo() - " + (System.currentTimeMillis() - n));

        n = System.currentTimeMillis();
        double[] arr = null;
        for (int i = 0, r = m.rows(); i < r; i++) {
            for (int j = 0, c = m.cols(); j < c; j++) {
                arr = m.get(i, j);
                arr[0] += 10;
                arr[1] += 10;
                arr[2] += 10;
                arr[3] += 10;
                m.put(i, j, arr);
            }
        }
        System.out.println("get() - " + (System.currentTimeMillis() - n));
        n = System.currentTimeMillis();
        Core.add(mat, new Scalar(10, 10, 10, 10), mat);
        System.out.println("add() - " + (System.currentTimeMillis() - n));

        mat = new Mat(4000, 4000, CvType.CV_8UC4, Scalar.all(20));
        n = System.currentTimeMillis();
        Core.subtract(mat, new Scalar(10, 10, 10, 10), mat);
        System.out.println("subtract() - " + (System.currentTimeMillis() - n));
        n = System.currentTimeMillis();
        Core.add(mat, new Scalar(10, 10, 10, 10), mat);
        System.out.println("add() - " + (System.currentTimeMillis() - n));

        Mat ma = new Mat(1, 4, CvType.CV_8UC1);
        ma.put(0, 0, 1, 2, 3, 4);
        System.out.println(ma.dump());
    }
}
