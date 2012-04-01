package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage

trait InterestingStructureFinder[Structure] {
  def findInterestingStructure(image: BufferedImage) : Structure
}
