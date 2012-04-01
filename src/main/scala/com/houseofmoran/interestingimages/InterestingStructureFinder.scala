package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage

trait InterestingStructureFinder[Structure] {
  def findInterestingStructure(image: BufferedImage) : Structure
}
