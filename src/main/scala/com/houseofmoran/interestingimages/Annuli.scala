package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.Shape
import java.awt.BasicStroke
import java.awt.Color

import com.houseofmoran.util.graphics.Graphics._

class Annuli(lhsProportions: (Double,Double), rhsProportions: (Double, Double), color: Color) {

	def this(lhsProportions: (Double,Double), rhsProportions: (Double, Double)) = {
		this(lhsProportions, rhsProportions, new Color(1.0f, 1.0f, 1.0f, 0.5f))
	}

	val lhsAnnulus = new Annulus(lhsProportions._1, lhsProportions._2, color)
	val rhsAnnulus = new Annulus(rhsProportions._1, rhsProportions._2, color)
	
	def drawIn(image: BufferedImage, rect: Rectangle2D) = {
		useGraphics(image) { g2 =>
			g2.drawRenderedImage(image, IDENTITY)
			val savedClip = g2.getClip
			g2.setClip(new Rectangle2D.Double(rect.getX, rect.getY, rect.getWidth / 2.0, rect.getHeight))
			lhsAnnulus.drawAnnulusIn(g2, rect)
			
			g2.setClip(new Rectangle2D.Double(rect.getX + (rect.getWidth / 2.0), rect.getY, rect.getWidth / 2.0, rect.getHeight))
			rhsAnnulus.drawAnnulusIn(g2, rect)
			
			g2.setClip(savedClip)
		}
	}
}