package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

trait ImageInformationEstimator {
	def estimateFor(image: BufferedImage, rect: Rectangle2D) : Double
}