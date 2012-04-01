package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.Color

import scala.collection.SortedSet

import java.util.logging.Logger

import com.houseofmoran.util.graphics.Graphics._

class QuadEstimatorComparerHighlighter extends InterestingStructureHighlighter[QuadTree[(Double,Double)], BufferedImage] {

	val logger = Logger.getLogger(this.getClass.getName)
	val identity = new AffineTransform()
	
	val clearGreen = new Color(0.0f, 1.0f, 0.0f, 0.5f)
	val clearRed = new Color(1.0f, 0.0f, 0.0f, 0.5f)
	
	def highlight(image: BufferedImage, structure: QuadTree[(Double,Double)]) : BufferedImage = {
		val rootRect = new Rectangle2D.Float(0.0f, 0.0f, image.getWidth, image.getHeight)
		val scoredRectanglesByLevel = structure.annotatedRectanglesByLevel(rootRect)
		
		val orderedLevels = SortedSet[Int]() ++ scoredRectanglesByLevel.keys
		logger.info("Num levels: " + orderedLevels.size)
		val highlighted = new BufferedImage(image.getWidth * orderedLevels.size, image.getHeight, image.getType)
		val forLevel = new BufferedImage(image.getWidth, image.getHeight, image.getType)
		
		for (level <- orderedLevels) {
			logger.info("Drawing level: " + level)
			useGraphics(forLevel) { g2 =>
				g2.drawRenderedImage(image, identity)
			}
			val scoresForBoth = scoredRectanglesByLevel(level).map(_._2)
			
			val minScoreA = scoresForBoth.map(_._1).reduceLeft((a, b) => (if (a > b) b else a))
			val maxScoreA = scoresForBoth.map(_._1).reduceLeft((a, b) => (if (a > b) a else b))
			val minScoreB = scoresForBoth.map(_._2).reduceLeft((a, b) => (if (a > b) b else a))
			val maxScoreB = scoresForBoth.map(_._2).reduceLeft((a, b) => (if (a > b) a else b))
			
			val maxScore = Math.max(maxScoreA, maxScoreB)
			
			for((rect, (scoreForA, scoreForB)) <- scoredRectanglesByLevel(level)) {
				logger.info("Rect: " + rect + " scoreForA: " + scoreForA + " scoreForB: " + scoreForB)
				val annuli = new Annuli (
						(minScoreA / maxScore, scoreForA / maxScore), 
						(minScoreB / maxScore, scoreForB / maxScore), 
						if (scoreForA > scoreForB) clearGreen else clearRed)
				annuli.drawIn(forLevel, rect)
			}
			useGraphics(highlighted) { g2 =>
				g2.drawRenderedImage(forLevel, AffineTransform.getTranslateInstance(level * image.getWidth, 0.0))
			}
		}
		
		highlighted
	}
}