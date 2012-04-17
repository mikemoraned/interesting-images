package com.houseofmoran.interestingimages

import java.awt.geom.Rectangle2D

trait PixelGrabber {
  def getPixels(region: Rectangle2D) : Array[Int]
}