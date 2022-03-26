package com.company.imagedownloading;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Chapter3 {
    public static String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\com\\company\\mouse.jpg";

    private static boolean isRun = true;
    private static boolean isEnd = false;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {
        Mat img = Imgcodecs.imread(path);
        if (img.empty())
            System.out.println("Failed image downloading!");
        else {
            System.out.println(img.width() + " x " + img.height());
            System.out.println(CvType.typeToString(img.type()));
            System.out.println(img.channels());
        }

        System.out.println(Imgcodecs.IMREAD_UNCHANGED); // -1
        System.out.println(Imgcodecs.IMREAD_GRAYSCALE); // 0
        System.out.println(Imgcodecs.IMREAD_COLOR); // 1
        System.out.println(Imgcodecs.IMREAD_ANYDEPTH); // 2
        System.out.println(Imgcodecs.IMREAD_ANYCOLOR); // 4
        System.out.println(Imgcodecs.IMREAD_LOAD_GDAL); // 8
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2); // 16
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_4); // 32
        System.out.println(Imgcodecs.IMREAD_REDUCED_GRAYSCALE_8); // 64
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_2); // 17
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_4); // 33
        System.out.println(Imgcodecs.IMREAD_REDUCED_COLOR_8); // 65
        System.out.println(Imgcodecs.IMREAD_IGNORE_ORIENTATION); // 128

        boolean st = Imgcodecs.imwrite(path, img);
        System.out.println(st);

        Mat m = new Mat(3, 2, CvType.CV_8UC1);
        double n = 1.0;
        for (int i = 0, r = m.rows(); i < r; i++) {
            for (int j = 0, c = m.cols(); j < c; j++) {
                m.put(i, j, n++);
            }
        }
        byte[] arr = new byte[m.channels() * m.cols() * m.rows()];
        System.out.println(m.get(0, 0, arr)); // 6
        System.out.println(Arrays.toString(arr)); // [1, 2, 3, 4, 5, 6]

        MatOfByte buf = new MatOfByte();
        st = Imgcodecs.imencode(".jpg", img, buf, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));

        System.out.println(st);

        Mat img2 = Imgcodecs.imdecode(buf, Imgcodecs.IMREAD_COLOR);
        System.out.println(img2);

        Mat matImg = Imgcodecs.imread(path);
        if (matImg.empty()) {
            System.out.println("OOOps, empty!");
            return;
        }
        CvUtils.showImage(matImg, "Hallo, mein bruders!");

        System.out.println(Videoio.CAP_FFMPEG);
        System.out.println(Videoio.CAP_IMAGES);
        System.out.println(Videoio.CAP_DSHOW);

        JFrame window = new JFrame("Watching video");
        window.setSize(1000, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JLabel label = new JLabel();
        window.setContentPane(label);
        window.setVisible(true);

        String path = "E:\\Android_Development\\Java\\Java_OpenCV\\src\\com\\company\\video.mp4";
        VideoCapture capture = new VideoCapture(path);
        if (!capture.isOpened()) {
            System.out.println("Failed open video");
            return;
        }
        Mat frame = new Mat();
        BufferedImage bufferedImage = null;
        while (capture.read(frame)) {
            Imgproc.resize(frame, frame, new Size(960, 540));
            // Здесь можно вставить код обработки кадра
            bufferedImage = CvUtils.MatToBufferedImage(frame);
            if (bufferedImage != null) {
                ImageIcon imageIcon = new ImageIcon(bufferedImage);
                label.setIcon(imageIcon);
                label.repaint();
                window.pack();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Выход");
        capture.release();

        VideoCapture videoCapture = new VideoCapture();
        if (!videoCapture.open(0)) {
            System.out.println("Failed connect to camera");
            return;
        }

        Mat matFrame = new Mat();
        BufferedImage bufferedImage1 = null;
        if (videoCapture.grab()) {
            if (videoCapture.retrieve(matFrame)) {
                BufferedImage image = CvUtils.MatToBufferedImage(matFrame);
                if (image != null) {
                    ImageIcon imageIcon = new ImageIcon(image);
                    label.setIcon(imageIcon);
                    label.repaint();
                }
            } else System.out.println("Failed to process a frame");
        } else System.out.println("Failed to take a frame");

        JFrame window1 = new JFrame(
                "Нажмите <Esc> для отключения от камеры");
        window1.setSize(640, 480);
        window1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window1.setLocationRelativeTo(null);
        // Обработка нажатия кнопки Закрыть в заголовке окна
        window1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRun = false;
                if (isEnd) {
                    window1.dispose();
                    System.exit(0);
                } else {
                    System.out.println(
                            "Сначала нажмите <Esc>, потом Закрыть");
                }
            }
        });
        // Обработка нажатия клавиши <Esc>
        window1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    isRun = false;
                }
            }
        });
        label = new JLabel();
        window1.setContentPane(label);
        window1.setVisible(true);
        // Подключаемся к камере
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            window1.setTitle("Не удалось подключиться к камере");
            isRun = false;
            isEnd = true;
            return;
        }

        try {
            // Задаем размеры кадра
            camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
            camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
            // Считываем кадры
            Mat frame2 = new Mat();
            BufferedImage image = null;
            while (isRun) {
                if (camera.read(frame2)) {
                    // Здесь можно вставить код обработки кадра
                    image = CvUtils.MatToBufferedImage(frame2);
                    if (image != null) {
                        ImageIcon imageIcon = new ImageIcon(image);
                        label.setIcon(imageIcon);
                        label.repaint();
                        window1.pack();
                    }
                    try {
                        Thread.sleep(100); // 10 кадров в секунду
                    } catch (InterruptedException e) {
                    }
                } else {
                    System.out.println("Не удалось захватить кадр");
                    break;
                }
            }
        } finally {
            camera.release();
            isRun = false;
            isEnd = true;
        }
        window1.setTitle("Камера отключена");
    }
}
