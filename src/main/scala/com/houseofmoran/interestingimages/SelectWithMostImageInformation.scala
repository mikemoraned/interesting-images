package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

import java.util.logging.Logger

class SelectWithMostImageInformation(proportion: Float, decorated: InterestingRegionFinder[Rectangle2D], estimator: ImageInformationEstimator) extends InterestingRegionFinder[Rectangle2D] {

	val logger = Logger.getLogger(this.getClass.getName)

	def findInterestingStructure(image: BufferedImage) : Seq[(Rectangle2D, Double)] = {
		val toFilter = decorated.findInterestingStructure(image).map(_._1)

		val scored = score(toFilter, image).toList
		val largestFirst = scored.sortWith( (lhs, rhs) => {
			rhs._2 < lhs._2
		})
		logger.info("Scored, largestFirst: " + largestFirst)

		val requiredSize = Math.ceil(proportion * largestFirst.size).toInt
		val selected = largestFirst.take(requiredSize)

		logger.info("Selected: " + selected)
		selected
	}

	def score(regions: Seq[Rectangle2D], image: BufferedImage) : Seq[(Rectangle2D, Double)] = {
		for (region <- regions) yield {
			(region, estimator.estimateFor(image, region))      
		}
	}
}
