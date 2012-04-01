package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

import java.util.Random

class ScaledRandomRects(seed: Int, scale: Float, numRects: Int) extends InterestingRegionFinder[Rectangle2D] {
  val random = new Random(seed)
  
  def findInterestingStructure(image: BufferedImage) : Seq[(Rectangle2D, Double)] = {
    val scaledWidth = Math.ceil(scale * image.getWidth()).asInstanceOf[Int]
    val scaledHeight = Math.ceil(scale * image.getHeight()).asInstanceOf[Int]
    for (rect <- 0 until numRects) yield {
      val left = random.nextInt(image.getWidth - scaledWidth);
      val top = random.nextInt(image.getHeight - scaledHeight);
      (new Rectangle2D.Float(left, top, scaledWidth, scaledHeight), 0.0)
    }
  }
}
