package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage

trait InterestingStructureHighlighter[Structure, Highlighted] {
  def highlight(image: BufferedImage, structure: Structure) : Highlighted
}