package com.houseofmoran.wikiblog.site.images

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

object Interesting {
  
  def defaultStatic(image: BufferedImage) : BufferedImage = {
    grid(new GzipStreamEstimator)(image)
  }
  
  def defaultAnimated(image: BufferedImage) : Array[Byte] = {
    animated(new GzipStreamEstimator)(image)
  }
  
  def defaultQuads(image: BufferedImage) : BufferedImage = {
	  nested(new GzipStreamEstimator)(image)
  }
  
  def noop(image: BufferedImage) = image
  
  def grid(estimator: ImageInformationEstimator)(image: BufferedImage) : BufferedImage = {
    val finder = new SelectWithMostImageInformation(1.0f, new ScaledRectilinearGridRects(10), estimator)
    val highlighter = new CircleRadiiAsScore
    highlighter.highlight(image, finder.findInterestingStructure(image))
  }
  
  def animated(estimator: ImageInformationEstimator)(image: BufferedImage) : Array[Byte] = {
    val finder = new ReverseOrder(new SelectWithMostImageInformation(1.0f, new ScaledRectilinearGridRects(10), estimator))
    val frameHighlighter = new IdentityHighlighter[Rectangle2D]
    val highlighter = new AnimateOverRegions(frameHighlighter, 10 * 1000, true)
    highlighter.highlight(image, finder.findInterestingStructure(image))
  }
  
  def nested(estimator: ImageInformationEstimator)(image: BufferedImage) : BufferedImage = {
	  val finder = new QuadScorer(new FullDepthQuadsGenerator(3), estimator)
	  val highlighter = new QuadStackHighlighter
	  highlighter.highlight(image, finder.findInterestingStructure(image))
  }

  def nestedComparison(estimatorA: ImageInformationEstimator, estimatorB: ImageInformationEstimator)(image: BufferedImage) : BufferedImage = {
	  val finder = new QuadEstimatorComparer(new FullDepthQuadsGenerator(3), estimatorA, estimatorB)
	  val highlighter = new QuadEstimatorComparerHighlighter
	  highlighter.highlight(image, finder.findInterestingStructure(image))
  }
}
