package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

import java.util.logging.Logger

class DirectPixelGrabber(image: BufferedImage) extends PixelGrabber {

	val logger = Logger.getLogger(this.getClass.getName)

	def getPixels(region: Rectangle2D) : Array[Int] = {
		val pixels = new Array[Int](image.getWidth * image.getHeight)
//		logger.info("Region: " + region)
		image.getRGB(region.getX.toInt, region.getY.toInt, region.getWidth.toInt, region.getHeight.toInt, pixels, 0, region.getWidth.toInt)
		pixels
	}
}