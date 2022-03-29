package labolatory

import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class AffineTransformation(private var image: Mat) {
    fun move(movingCoif: Double): Mat {
        val p1 = Point(5.0, 5.0)
        val p2 = Point(20.0, 5.0)
        val p3 = Point(5.0, 20.0)

        val p4 = Point(p1.x + movingCoif, p1.y + movingCoif)
        val p5 = Point(p2.x + movingCoif, p2.y + movingCoif)
        val p6 = Point(p3.x + movingCoif, p3.y + movingCoif)

        val ma1 = MatOfPoint2f(p1, p2, p3)
        val ma2 = MatOfPoint2f(p4, p5, p6)
        val resMat = Imgproc.getAffineTransform(ma1, ma2)

        val changedImage = Mat()

        val size = Size(image.cols().toDouble(), image.cols().toDouble())

        Imgproc.warpAffine(image, changedImage, resMat, size)
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

    fun shift(
        point1: Double,
        point2: Double,
        point3: Double,
        point4: Double,
        point5: Double,
        point6: Double,
    ): Mat {
        val originalPoints = arrayOf(
            Point(0.0, 0.0),
            Point((image.cols() - 1).toDouble(), 0.0),
            Point(0.0, (image.rows() - 1).toDouble())
        )

        val changedPoints = arrayOf(
            Point(image.cols() * point1, image.rows() * point2),
            Point(image.cols() * point3, image.rows() * point4),
            Point(image.cols() * point5, image.rows() * point6)
        )

        val mofSrc = MatOfPoint2f(*originalPoints)
        val mofDst = MatOfPoint2f(*changedPoints)

        val gAffine1 = Imgproc.getAffineTransform(mofSrc, mofDst)

        val warpAffineDst1 = Mat()
        Imgproc.warpAffine(image, warpAffineDst1, gAffine1, image.size())

        return warpAffineDst1
    }

}