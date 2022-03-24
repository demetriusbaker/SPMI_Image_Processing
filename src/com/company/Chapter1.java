package com.company;

import org.opencv.core.*;

public class Chapter1 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        System.out.println(Core.VERSION);
        System.out.println(Core.VERSION_MAJOR);
        System.out.println(Core.VERSION_MINOR);
        System.out.println(Core.VERSION_REVISION);
        System.out.println(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.getBuildInformation());

        Point point = new Point();
        double x = point.x;
        double y = point.y;
        System.out.println("X=" + x + " Y=" + y);
        System.out.println(point);

        point = new Point(3.14324, 6.3453);
        System.out.println(point);

        point = new Point(new double[]{2.3, 5});
        System.out.println(point);

        point.set(new double[]{4.4, 5.6});
        System.out.println(point);

        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 2);
        System.out.println(point1 + " " + point2);
        System.out.println(point1.equals(point2));

        Point pointOrig = new Point(3, 4);
        Point pointCopy = pointOrig;
        pointCopy.set(new double[]{1, 9.54});
        System.out.println(pointOrig);

        pointOrig = new Point(3, 4);
        Point rightCopyPoint = pointOrig.clone();
        System.out.println(rightCopyPoint);

        Point p = new Point(9.0, 5.0);
        Rect r = new Rect(0, 0, 10, 10);
        Rect r2 = new Rect(10, 5, 10, 10);
        System.out.println(p.inside(r));
        System.out.println(p.inside(r2));

        Point3 point3 = new Point3();
        System.out.println(point3.x);
        System.out.println(point3.y);
        System.out.println(point3.z);
        System.out.println(point3);

        point3 = new Point3(10, 20, 5.5);
        System.out.println(point3.x);
        System.out.println(point3.y);
        System.out.println(point3.z);
        System.out.println(point3);

        point3 = new Point3(new double[]{1.2, 3.4, 5.6});
        System.out.println(point3.x);
        System.out.println(point3.y);
        System.out.println(point3.z);
        System.out.println(point3);

        point3 = new Point3(new Point(5, 5));
        System.out.println(point3.x);
        System.out.println(point3.y);
        System.out.println(point3.z);
        System.out.println(point3);

        Size s = new Size();
        System.out.println(s);
        System.out.println(s.width);
        System.out.println(s.height);

        s = new Size(100.0, 50.0);
        System.out.println(s);
        System.out.println(s.width);
        System.out.println(s.height);

        s = new Size(new double[]{200.5, 564.3});
        System.out.println(s);
        System.out.println(s.width);
        System.out.println(s.height);

        s = new Size(new Point(546.0999, 123.098));
        System.out.println(s);
        System.out.println(s.width);
        System.out.println(s.height);

        Size emptySize = new Size();
        System.out.println(emptySize.empty());

        emptySize = new Size(1, 0);
        System.out.println(emptySize.empty());

        emptySize = new Size(0, 3);
        System.out.println(emptySize.empty());

        emptySize = new Size(5, 7);
        System.out.println(emptySize.empty());

        Rect rect = new Rect();
        System.out.println(rect);
        rect.x = 0;
        rect.y = 10;
        rect.width = 100;
        rect.height = 50;
        System.out.println(rect.x + " " + rect.y);
        System.out.println(rect.width + " " + rect.height);

        rect = new Rect(0, 0, 100, 50);
        System.out.println(rect);

        rect = new Rect(new double[]{0.0, 0.0, 100.0, 50.0});
        System.out.println(rect);

        rect = new Rect(new Point(1, 3), new Point(5, 6));
        System.out.println(rect);

        rect = new Rect(new Point(1, 3), new Size(50.0, 60.5));
        System.out.println(rect);

        rect = new Rect(10, 5, 100, 50);
        System.out.println(rect.tl() + " " + rect.br());
        System.out.println(rect.area());
        System.out.println(rect.contains(new Point(10.5, 5.0)));
        System.out.println(rect.contains(new Point(-10.5, 5.0)));

        RotatedRect rotatedRect = new RotatedRect();
        rotatedRect.center = new Point(50, 50);
        rotatedRect.size = new Size(100.0, 100.0);
        rotatedRect.angle = 45.0;
        System.out.println(rotatedRect);

        rotatedRect = new RotatedRect(new Point(50, 50), new Size(100, 200), 30);
        System.out.println(rotatedRect);

        rotatedRect = new RotatedRect(new double[]{1, 2, 3, 4, 5, 6});
        System.out.println(rotatedRect);
        System.out.println(rotatedRect.boundingRect());

        // +31 of 312

        Scalar scalar = new Scalar(1.0);
        System.out.println(scalar);

        scalar = new Scalar(new double[]{1, 2, 3, 4});
        System.out.println(scalar);

        scalar = new Scalar(4, 5, 9, 8);
        System.out.println(scalar);

        for (int i = 0; i < 4; i++) {
            System.out.println(scalar.val[i]);
        }

        scalar = Scalar.all(999);
        System.out.println(scalar);

        scalar.set(new double[]{8, 8, 0, 0});
        System.out.println(scalar);

        scalar = new Scalar(10, 0, 0, 0);
        System.out.println(scalar.isReal());

        scalar = Scalar.all(1);
        System.out.println(scalar.isReal());

        scalar = new Scalar(1, -3, 5, -8);
        System.out.println(scalar.conj());

        System.out.println(scalar.mul(Scalar.all(5)));

        Range range = new Range();
        range.start = 1;
        range.end = 100;
        System.out.println(range);

        range = new Range(2, 51);
        System.out.println(range);

        range = new Range(new double[]{5, 28});
        System.out.println(range);

        range = Range.all();
        System.out.println(range);
        System.out.println(range.size());

        range.set(new double[]{4, 16});
        System.out.println(range);

        range = new Range(0, 0);
        System.out.println(range.empty());

        range = new Range(1, 51);
        System.out.println(range.shift(10));

        Range range1 = new Range(1, 51);
        Range range2 = new Range(30, 81);
        System.out.println(range1.intersection(range2));
    }
}
