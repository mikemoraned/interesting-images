package com.houseofmoran.interestingimages

import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

class QuadEstimatorComparer(decorated: InterestingStructureFinder[QuadTree[Double]], estimatorA: ImageInformationEstimator, estimatorB: ImageInformationEstimator) 
  extends InterestingStructureFinder[QuadTree[(Double,Double)]] {
    def findInterestingStructure(image: BufferedImage) : QuadTree[(Double,Double)] = {
      val rootRect = new Rectangle2D.Float(0.0f, 0.0f, image.getWidth, image.getHeight)
      decorated.findInterestingStructure(image).annotate(rootRect) { 
	rect =>
	  (estimatorA.estimateFor(image, rect), estimatorB.estimateFor(image, rect))
      }
    }
}
