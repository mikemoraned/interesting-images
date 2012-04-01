package com.houseofmoran.wikiblog.site.images

import java.awt.geom.Rectangle2D

trait PixelGrabber {
  def getPixels(region: Rectangle2D) : Array[Int]
}