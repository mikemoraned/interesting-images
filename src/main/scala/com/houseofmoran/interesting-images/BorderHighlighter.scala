package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.Color

import java.util.logging.Logger

import com.houseofmoran.util.graphics.Graphics._

class BorderHighlighter extends InterestingRegionHighlighter[Shape, BufferedImage] {
  val logger = Logger.getLogger(this.getClass.getName)
  val identity = new AffineTransform()
  
  def highlight(image: BufferedImage, regions: Seq[(Shape, Double)]) : BufferedImage = {
    logger.info(regions.size + " regions seen")
    
    val highlighted = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    
    useGraphics(highlighted) { g2 =>
      g2.drawRenderedImage(image, identity)
      g2.setColor(Color.white)
      for(region <- regions.map(_._1)) {
        g2.draw(region)
      }
    }
    
    highlighted
  }
}
