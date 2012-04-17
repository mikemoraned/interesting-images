package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.Shape
import java.awt.BasicStroke
import java.awt.Color

import java.util.logging.Logger
import com.houseofmoran.util.graphics.Graphics._

class CircleRadiiAsScore extends InterestingRegionHighlighter[Rectangle2D, BufferedImage] {
  val logger = Logger.getLogger(this.getClass.getName)
  
  def highlight(image: BufferedImage, regions: Seq[(Rectangle2D, Double)]) : BufferedImage = {
    logger.info(regions.size + " regions seen")
    
    val highlighted = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    
    val maxScore = regions.map(_._2).reduceLeft((a, b) => (if (a > b) a else b))
    val minScore = regions.map(_._2).reduceLeft((a, b) => (if (a < b) a else b))
    
    useGraphics(highlighted) { g2 =>
      g2.drawRenderedImage(image, IDENTITY)
    }

    for((rect, score) <- regions) {
    	new Annulus(minScore / maxScore, score / maxScore).drawIn(highlighted, rect)
    }
    
    highlighted
  }
}

