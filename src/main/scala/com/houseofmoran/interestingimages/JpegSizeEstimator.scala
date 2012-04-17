package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import javax.imageio._
import java.io.ByteArrayOutputStream

class JpegSizeEstimator extends ImageInformationEstimator {
	def estimateFor(image: BufferedImage, rect: Rectangle2D) : Double = {
		val subImage = image.getSubimage(rect.getX.toInt, rect.getY.toInt, rect.getWidth.toInt, rect.getHeight.toInt)
		formatSize(subImage, "jpeg") / formatSize(subImage, "bmp")
	}
	
	def formatSize(image: BufferedImage, name: String) : Double = {
		val out = new ByteArrayOutputStream
		ImageIO.write(image, name, out)
		out.toByteArray.length.toDouble
	}
}