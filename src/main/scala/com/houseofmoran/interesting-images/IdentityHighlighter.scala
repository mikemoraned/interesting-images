package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.Shape

class IdentityHighlighter[Region <: Shape]  extends InterestingRegionHighlighter[Region, BufferedImage] {
  
  def highlight(image: BufferedImage, regions: Seq[(Region, Double)]) : BufferedImage = {
    image
  }
}
