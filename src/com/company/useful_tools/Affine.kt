package com.company.useful_tools

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class Affine(private var image: Mat) {
    fun scale(scale: Double): Mat {
        val changedImage = Mat()
        val width = image.width() * scale
        val height = image.height() * scale
        Imgproc.resize(image, changedImage, Size(width, height))
        return changedImage
    }

    fun turn(degree: Double): Mat {
        val originalImage = mat2BufferedImage(image)
        val w = originalImage.width
        val h = originalImage.height
        val toRad = Math.toRadians(degree)
        val hPrime = (w * abs(sin(toRad)) + h * abs(cos(toRad)))
        val wPrime = (h * abs(sin(toRad)) + w * abs(cos(toRad)))
        val rotatedImage = BufferedImage(wPrime.toInt(), hPrime.toInt(), BufferedImage.TYPE_INT_RGB)
        val g = rotatedImage.createGraphics()
        g.color = Color.BLACK
        g.fillRect(0, 0, wPrime.toInt(), hPrime.toInt())
        g.translate(wPrime / 2, hPrime / 2)
        g.rotate(toRad)
        g.translate(-w / 2, -h / 2)
        g.drawImage(originalImage, 0, 0, null)
        g.dispose()
        return bufferedImage2Mat(rotatedImage)
    }

    fun shift(x: Double, y: Double): Mat {
        val originalPoints = arrayOf(
            Point(0.0, 0.0),
            Point((image.cols() - 1).toDouble(), 0.0),
            Point(0.0, (image.rows() - 1).toDouble())
        )

        val changedPoints = arrayOf(
            Point(image.cols() * x, image.rows() * y),
            Point(image.cols() * 1.0, image.rows() * 0.0),
            Point(image.cols() * 0.0, image.rows() * 1.0)
        )

        val mofSrc = MatOfPoint2f(*originalPoints)
        val mofDst = MatOfPoint2f(*changedPoints)

        val gAffine1 = Imgproc.getAffineTransform(mofSrc, mofDst)

        val warpAffineDst1 = Mat()
        Imgproc.warpAffine(image, warpAffineDst1, gAffine1, image.size())

        return warpAffineDst1
    }

    companion object {
        fun bufferedImage2Mat(sourceImg: BufferedImage): Mat {
            val dataBuffer = sourceImg.raster.dataBuffer
            var imgPixels: ByteArray? = null
            var imgMat: Mat? = null
            val width = sourceImg.width
            val height = sourceImg.height
            if (dataBuffer is DataBufferByte) imgPixels = dataBuffer.data
            if (dataBuffer is DataBufferInt) {
                val byteSize = width * height
                imgPixels = ByteArray(byteSize * 3)
                val imgIntegerPixels = dataBuffer.data
                for (p in 0 until byteSize) {
                    imgPixels[p * 3 + 2] = (imgIntegerPixels[p] and 0x00FF0000 shr 16).toByte()
                    imgPixels[p * 3 + 1] = (imgIntegerPixels[p] and 0x0000FF00 shr 8).toByte()
                    imgPixels[p * 3 + 0] = (imgIntegerPixels[p] and 0x000000FF).toByte()
                }
            }
            if (imgPixels != null) {
                imgMat = Mat(height, width, CvType.CV_8UC3)
                imgMat.put(0, 0, imgPixels)
            }
            return imgMat!!
        }

        fun mat2BufferedImage(matrix: Mat?): BufferedImage {
            val mob = MatOfByte()
            Imgcodecs.imencode(".jpg", matrix, mob)
            return ImageIO.read(ByteArrayInputStream(mob.toArray()))
        }
    }
}