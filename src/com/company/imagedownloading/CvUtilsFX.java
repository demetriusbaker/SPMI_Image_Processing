package com.company.imagedownloading;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

public class CvUtilsFX extends Application {
    public static WritableImage MatToWritableImage(Mat m) {
        BufferedImage bim = CvUtils.MatToBufferedImage(m);
        if (bim == null) return null;
        else return SwingFXUtils.toFXImage(bim, null);
    }

    public static WritableImage MatToImageFX(Mat m) {
        if (m == null || m.empty()) return null;
        if (m.depth() == CvType.CV_8U) {
        } else if (m.depth() == CvType.CV_16U) {
            Mat m_16 = new Mat();
            m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            m = m_16;
        } else if (m.depth() == CvType.CV_32F) {
            Mat m_32 = new Mat();
            m.convertTo(m_32, CvType.CV_8U, 255);
            m = m_32;
        } else
            return null;
        if (m.channels() == 1) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_GRAY2BGRA);
            m = m_bgra;
        } else if (m.channels() == 3) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_BGR2BGRA);
            m = m_bgra;
        } else if (m.channels() == 4) {
        } else
            return null;
        byte[] buf = new byte[m.channels() * m.cols() * m.rows()];
        m.get(0, 0, buf);
        WritableImage wim = new WritableImage(m.cols(), m.rows());
        PixelWriter pw = wim.getPixelWriter();
        pw.setPixels(0, 0, m.cols(), m.rows(),
                WritablePixelFormat.getByteBgraInstance(),
                buf, 0, m.cols() * 4);
        return wim;
    }

    public static Mat ImageFXToMat(Image img) {
        if (img == null) return new Mat();
        PixelReader pr = img.getPixelReader();
        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        byte[] buf = new byte[4 * w * h];
        pr.getPixels(0, 0, w, h, WritablePixelFormat.getByteBgraInstance(),
                buf, 0, w * 4);
        Mat m = new Mat(h, w, CvType.CV_8UC4);
        m.put(0, 0, buf);
        return m;
    }

    public static void showImage(Mat img, String title) {
        Image im = MatToImageFX(img);
        Stage window = new Stage();
        ScrollPane sp = new ScrollPane();
        ImageView iv = new ImageView();
        if (im != null) {
            iv.setImage(im);
            if (im.getWidth() < 1000) {
                sp.setPrefWidth(im.getWidth() + 5);
            } else sp.setPrefWidth(1000.0);
            if (im.getHeight() < 700) {
                sp.setPrefHeight(im.getHeight() + 5);
            } else sp.setPrefHeight(700.0);
        }
        sp.setContent(iv);
        sp.setPannable(true);
        BorderPane box = new BorderPane();
        box.setCenter(sp);
        Scene scene = new Scene(box);
        window.setScene(scene);
        window.setTitle(title);
        window.show();
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(15.0);
        root.setAlignment(Pos.CENTER);

        Button button = new Button("Perform");
        button.setOnAction(this::onClickButton);
        root.getChildren().add(button);

        Scene scene = new Scene(root, 400.0, 150.0);
        stage.setTitle("OpenCV " + Core.VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> Platform.exit());
        stage.show();

        Mat img = Imgcodecs.imread("E:\\Android_Development\\Java\\Java_OpenCV\\src\\com\\company\\mouse.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat img2 = new Mat(70, 390, CvType.CV_8UC3, CvUtils.COLOR_BLACK);
        Imgproc.putText(img2, "St. Petersburg", new Point(10, 50),
                6, 3, CvUtils.COLOR_WHITE,
                3, Imgproc.LINE_AA, false);
        Mat roi = img.submat(new Rect(0, 0, img2.width(), img2.height()));
        img2.copyTo(roi);
        CvUtilsFX.showImage(img, "Результат");
        img.release();
        img2.release();
    }

    private void onClickButton(ActionEvent actionEvent) {
        String path = "E:\\Android_Development\\Java\\OpenCV\\src\\com\\company\\mouse.jpg";
        Mat img = Imgcodecs.imread(path);
        if (img.empty()) {
            System.out.println("OOOps, empty!");
            return;
        }
        CvUtilsFX.showImage(img, "Text in the window title");
    }
}
