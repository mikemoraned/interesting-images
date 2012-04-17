package com.houseofmoran.interestingimages

import javax.imageio.stream.MemoryCacheImageOutputStream
import javax.imageio.ImageIO

import net.kroo.elliot.GifSequenceWriter

import java.awt.image.BufferedImage
import java.awt.Shape
import java.awt.geom.Area

import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

import java.util.logging.Logger

import com.houseofmoran.util.graphics.Graphics._

class AnimateOverRegions[Region <: Shape](frameHighlighter: InterestingRegionHighlighter[Region, BufferedImage], totalTime: Int, loop: Boolean) extends InterestingRegionHighlighter[Region, Array[Byte]] {
  val IMAGE_TYPE = BufferedImage.TYPE_BYTE_INDEXED
  
  val logger = Logger.getLogger(this.getClass.getName)
  
  def highlight(image: BufferedImage, regions: Seq[(Region, Double)]) : Array[Byte] = {
    val out = new ByteArrayOutputStream
    val imageOut = new MemoryCacheImageOutputStream(out)
    val timeBetweenFrames = totalTime / regions.size
    val writer = 
      new GifSequenceWriter(imageOut, IMAGE_TYPE, timeBetweenFrames, loop)
    val baseImage = frameHighlighter.highlight(image, regions)
    for(take <- 1 to regions.size) {
      logger.info("Doing " + take)
      val frameImage = new BufferedImage(image.getWidth, image.getHeight, IMAGE_TYPE)
      useGraphics(frameImage) { g2 =>
        val clipRegion = unionShapes(regions.take(take).map(_._1))
        g2.setClip(clipRegion)
        g2.drawRenderedImage(baseImage, IDENTITY)
      }
      writer.writeToSequence(frameImage)
    }
    writer.close
    imageOut.flush
    imageOut.close
    out.toByteArray
  }
}
