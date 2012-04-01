package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform

import java.util.logging.Logger
import java.awt.Shape
import java.awt.Graphics2D
import java.awt.Color

class OpacityClipHighlighter extends InterestingRegionHighlighter[Shape, BufferedImage] {
  val logger = Logger.getLogger(this.getClass.getName)
  val identity = new AffineTransform()
  
  def highlight(image: BufferedImage, regions: Seq[(Shape, Double)]) : BufferedImage = {
    logger.info(regions.size + " regions seen")
    
    val highlighted = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    
    val g2 = highlighted.createGraphics
    g2.setColor(Color.BLACK)
    g2.draw(new Rectangle2D.Float(0.0f, 0.0f, image.getWidth, image.getHeight))
    try {
      val maxScore = regions.map(_._2).reduceLeft((a, b) => (if (a > b) a else b))
      logger.info("max score: " + maxScore)
      for((shape, score) <- regions) {
        val alpha = 1.0 - (score / maxScore)
//        logger.info("Alpha: " + alpha)
        g2.setClip(shape)
        g2.drawRenderedImage(image, identity)
        g2.setColor(new Color(1.0f, 1.0f, 1.0f, alpha.toFloat))
        g2.fill(shape)
        g2.setColor(Color.RED)
        g2.draw(shape)
      }
    }
    finally {
      g2.dispose
    }
    
    highlighted
  }
}
