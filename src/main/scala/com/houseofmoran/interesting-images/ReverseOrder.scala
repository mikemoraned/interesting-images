package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.Shape

class ReverseOrder[Region <: Shape](decorated: InterestingRegionFinder[Region]) extends InterestingRegionFinder[Region] {
  def findInterestingStructure(image: BufferedImage) : Seq[(Region, Double)] = {
    val regions = decorated.findInterestingStructure(image)
    regions.toList.reverse
  }
}
