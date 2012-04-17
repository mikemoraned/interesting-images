package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.Color

import scala.collection.SortedSet

import java.util.logging.Logger

import com.houseofmoran.util.graphics.Graphics._

class QuadStackHighlighter extends InterestingStructureHighlighter[QuadTree[Double], BufferedImage] {
	
	val logger = Logger.getLogger(this.getClass.getName)
	val identity = new AffineTransform()
	
	def highlight(image: BufferedImage, structure: QuadTree[Double]) : BufferedImage = {
		val rootRect = new Rectangle2D.Float(0.0f, 0.0f, image.getWidth, image.getHeight)
		val scoredRectanglesByLevel = structure.annotatedRectanglesByLevel(rootRect)
		
		val orderedLevels = SortedSet[Int]() ++ scoredRectanglesByLevel.keys
		logger.info("Num levels: " + orderedLevels.size)
		val highlighted = new BufferedImage(image.getWidth * orderedLevels.size, image.getHeight, image.getType)
		val forLevel = new BufferedImage(image.getWidth, image.getHeight, image.getType)
		
		for (level <- orderedLevels) {
			logger.info("Drawing level: " + level)
			val scores = scoredRectanglesByLevel(level).map(_._2)
			val maxScore = scores.reduceLeft((a, b) => (if (a > b) a else b))
            val minScore = scores.reduceLeft((a, b) => (if (a < b) a else b))
			useGraphics(forLevel) { g2 =>
				g2.drawRenderedImage(image, identity)
			}
			for((rect, score) <- scoredRectanglesByLevel(level)) {
				logger.info("Rect: " + rect + " score: " + score)
				new Annulus(minScore / maxScore, score / maxScore).drawIn(forLevel, rect)
			}
			useGraphics(highlighted) { g2 =>
				g2.drawRenderedImage(forLevel, AffineTransform.getTranslateInstance(level * image.getWidth, 0.0))
			}
		}
		
		highlighted
	}
}