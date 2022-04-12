package laboratory.lab3

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class Affine(private var image: Mat) {
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

        val size = Size(image.cols().toDouble() + movingCoif, image.rows().toDouble() + movingCoif)
        Imgproc.warpAffine(image, changedImage, resMat, size)
        return changedImage
    }

    fun scale(scale: Double): Mat {
        val changedImage = Mat()
        val width = image.width() * scale
        val height = image.height() * scale
        Imgproc.resize(image, changedImage, Size(width, height))
        return changedImage
    }

    fun turn(flipCode: Int): Mat {
        val changedImage = Mat()
        Core.flip(image, changedImage, flipCode)
        return changedImage
    }

    fun shift(arr: Array<Double>): Mat {
        val originalPoints = arrayOf(
            Point(0.0, 0.0),
            Point((image.cols() - 1).toDouble(), 0.0),
            Point(0.0, (image.rows() - 1).toDouble())
        )

        val changedPoints = arrayOf(
            Point(image.cols() * arr[0], image.rows() * arr[1]),
            Point(image.cols() * arr[2], image.rows() * arr[3]),
            Point(image.cols() * arr[4], image.rows() * arr[5])
        )

        val mofSrc = MatOfPoint2f(*originalPoints)
        val mofDst = MatOfPoint2f(*changedPoints)

        val gAffine1 = Imgproc.getAffineTransform(mofSrc, mofDst)

        val warpAffineDst1 = Mat()
        Imgproc.warpAffine(image, warpAffineDst1, gAffine1, image.size())

        return warpAffineDst1
    }
}