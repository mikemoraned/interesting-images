package com.houseofmoran.util.graphics

import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.geom.Area
import java.awt.geom.AffineTransform

object Graphics {
  
  val IDENTITY = new AffineTransform

  def useGraphics(image: BufferedImage)(fn: Graphics2D => Unit) = {
    val g2 = image.getGraphics.asInstanceOf[Graphics2D]
    try {
      fn(g2)
    }
    finally {
      g2.dispose
    }
  }
  
  def unionShapes[S <: Shape](shapes: Seq[S]) : Area = {
    val areas = shapes.map(new Area(_))
    areas.reduceLeft((lhs,rhs) => {
      lhs.add(rhs)
      lhs
    })
  }
}
