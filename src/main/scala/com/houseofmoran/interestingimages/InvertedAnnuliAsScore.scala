package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.image.RescaleOp

import java.util.logging.Logger
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.Shape
import java.awt.Color

import com.houseofmoran.util.graphics.Graphics._

class InvertedAnnuliAsScore extends InterestingRegionHighlighter[Rectangle2D, BufferedImage] {
  val logger = Logger.getLogger(this.getClass.getName)
  
  def highlight(image: BufferedImage, regions: Seq[(Rectangle2D, Double)]) : BufferedImage = {
    logger.info(regions.size + " regions seen")
    
    val highlighted = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    
    val maxScore = regions.map(_._2).reduceLeft((a, b) => (if (a > b) a else b))
    val minScore = regions.map(_._2).reduceLeft((a, b) => (if (a < b) a else b))
    
    useGraphics(highlighted) { g2 =>
      val annuli = new Area
      
      var innerCircle = new Ellipse2D.Double(0.0, 0.0, 0.0, 0.0)
      var outerCircle = new Ellipse2D.Double(0.0, 0.0, 0.0, 0.0)
      
      for((rect, score) <- regions) {
        val maxDiameter = Math.min(rect.getWidth, rect.getHeight)
        val outerDiameter = (score / maxScore) * maxDiameter
        outerCircle.setFrame(rect.getCenterX - (outerDiameter / 2.0), rect.getCenterY - (outerDiameter / 2.0), outerDiameter, outerDiameter)
        annuli.add(new Area(outerCircle))
      }
      
      for((rect, score) <- regions) {
        val maxDiameter = Math.min(rect.getWidth, rect.getHeight)
        val innerDiameter = (minScore / maxScore) * maxDiameter
        innerCircle.setFrame(rect.getCenterX - (innerDiameter / 2.0), rect.getCenterY - (innerDiameter / 2.0), innerDiameter, innerDiameter)
        annuli.subtract(new Area(innerCircle))
      }

      g2.drawRenderedImage(image, IDENTITY)
      g2.setClip(annuli)
      g2.drawRenderedImage(applyImageOp(image), IDENTITY)
    }
    
    highlighted
  }
  
  def applyImageOp(image: BufferedImage) : BufferedImage = {
    val op = new RescaleOp(-1.0f, 255f, null)
    op.filter(image, null);
  }
}

