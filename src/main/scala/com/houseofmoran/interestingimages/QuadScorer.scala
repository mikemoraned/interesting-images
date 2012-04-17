package com.houseofmoran.interestingimages

import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

class QuadScorer(decorated: InterestingStructureFinder[QuadTree[Double]], estimator: ImageInformationEstimator) extends InterestingStructureFinder[QuadTree[Double]] {
	def findInterestingStructure(image: BufferedImage) : QuadTree[Double] = {
	  val rootRect = new Rectangle2D.Float(0.0f, 0.0f, image.getWidth, image.getHeight)
	  decorated.findInterestingStructure(image).annotate(rootRect) { 
	    rect =>
	      estimator.estimateFor(image, rect).toDouble
	  }
	}
}
