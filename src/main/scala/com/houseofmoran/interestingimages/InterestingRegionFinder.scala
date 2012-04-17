package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.Shape

trait InterestingRegionFinder[Region <: Shape] extends InterestingStructureFinder[Seq[(Region, Double)]] {
  def findInterestingStructure(image: BufferedImage) : Seq[(Region, Double)]
}
