package labolatory

import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class AffineTransformation(private var image: Mat) {
    fun move(): Mat {
        val changedImage = Mat()

        Imgproc.warpAffine(
            image,
            image,
            changedImage,
            Size(
                image.cols().toDouble(),
                image.rows().toDouble()
            )
        )
        return changedImage
    }

    fun scale(scale: Double): Mat = turn(0.0, scale)

    fun turn(flipCode: Int): Mat {
        val changedImage = Mat()
        Core.flip(image, changedImage, flipCode)
        return changedImage
    }

    fun turn(degree: Double, scale: Double): Mat {
        val changedImage = Mat()
        val m = Imgproc.getRotationMatrix2D(
            Point(
                (image.width() / 2).toDouble(),
                (image.height() / 2).toDouble()
            ),
            degree,
            scale
        )

        Imgproc.warpAffine(
            image,
            changedImage,
            m,
            Size(image.width().toDouble(), image.height().toDouble()),
            Imgproc.INTER_LINEAR,
            Core.BORDER_TRANSPARENT,
            Scalar(0.0, 0.0, 0.0, 255.0))

        return changedImage
    }

    fun shift(): Mat {
        val originalPoints = arrayOf(
            Point(0.0, 0.0),
            Point((image.cols() - 1).toDouble(), 0.0),
            Point(0.0, (image.rows() - 1).toDouble())
        )

        val changedPoints = arrayOf(
            Point(image.cols() * 0.0, image.rows() * 0.33),
            Point(image.cols() * 0.85, image.rows() * 0.25),
            Point(image.cols() * 0.15, image.rows() * 0.70)
        )

        val mofSrc = MatOfPoint2f(*originalPoints)
        val mofDst = MatOfPoint2f(*changedPoints)

        val gAffine1 = Imgproc.getAffineTransform(mofSrc, mofDst)

        val warpAffineDst1 = Mat()
        Imgproc.warpAffine(image, warpAffineDst1, gAffine1, image.size())

        return warpAffineDst1
    }

}