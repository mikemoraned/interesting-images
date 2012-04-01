package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

class ScaledRectilinearGridRects(gridCellsPerSide: Int) extends InterestingRegionFinder[Rectangle2D] {
  
  def findInterestingStructure(image: BufferedImage) : Seq[(Rectangle2D, Double)] = {
    val cellWidth = image.getWidth.toFloat / gridCellsPerSide
    val cellHeight = image.getHeight.toFloat / gridCellsPerSide
    for (xCell <- 0 until gridCellsPerSide;
         yCell <- 0 until gridCellsPerSide) yield {
      val left = xCell * cellWidth
      val top = yCell * cellHeight
      (new Rectangle2D.Float(left, top, cellWidth, cellHeight), 0.0)
    }
  }
}
