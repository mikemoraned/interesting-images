package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.Shape

trait InterestingRegionHighlighter[Region <: Shape, Highlighted] extends InterestingStructureHighlighter[Seq[(Region,Double)], Highlighted] {
  def highlight(image: BufferedImage, regions: Seq[(Region, Double)]) : Highlighted
}
