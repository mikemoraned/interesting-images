package com.houseofmoran.wikiblog.site.images

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

class Annulus(innerProportion: Double, outerProportion: Double, color: Color) {

	def this(innerProportion: Double, outerProportion: Double) = {
		this(innerProportion, outerProportion, new Color(1.0f, 1.0f, 1.0f, 0.5f))
	}
	
	val defaultStroke = new BasicStroke
    
	var dash = new Array[Float](2)
	dash(0) = 2.0f
	dash(1) = 5.0f
	var stepped = new BasicStroke(defaultStroke.getLineWidth, defaultStroke.getEndCap, defaultStroke.getLineJoin, defaultStroke.getMiterLimit, dash, 0.0f)
     
	def drawIn(image: BufferedImage, rect: Rectangle2D) = {
		useGraphics(image) { g2 =>
			g2.drawRenderedImage(image, IDENTITY)
			drawAnnulusIn(g2, rect)
		}
	}
	
	def drawAnnulusIn(g2: Graphics2D, rect: Rectangle2D) = {
		var innerCircle = new Ellipse2D.Double(0.0, 0.0, 0.0, 0.0)
		var outerCircle = new Ellipse2D.Double(0.0, 0.0, 0.0, 0.0)
		g2.setStroke(defaultStroke)
		val maxDiameter = Math.min(rect.getWidth, rect.getHeight)
		val outerDiameter = outerProportion * maxDiameter
		outerCircle.setFrame(rect.getCenterX - (outerDiameter / 2.0), rect.getCenterY - (outerDiameter / 2.0), outerDiameter, outerDiameter)
		val innerDiameter = innerProportion * maxDiameter
		innerCircle.setFrame(rect.getCenterX - (innerDiameter / 2.0), rect.getCenterY - (innerDiameter / 2.0), innerDiameter, innerDiameter)
		val annulus = new Area(outerCircle)
		annulus.subtract(new Area(innerCircle))

		g2.setColor(color)
		g2.draw(rect)
		g2.fill(annulus)

		g2.setColor(Color.BLACK)
		g2.setStroke(stepped)
		g2.draw(annulus)
	}
}