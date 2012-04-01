package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage
import java.awt.geom.Rectangle2D

import java.io._

abstract class ArrayCompressedSizeEstimator extends ImageInformationEstimator {
    def estimateFor(image: BufferedImage, rect: Rectangle2D) : Double = {
    	val grabber = new DirectPixelGrabber(image)
    	val pixels = grabber.getPixels(rect)
    	size(pixels, compressorAround) / size(pixels, noop)
    }
    
	def size(ints: Array[Int], wrapper: OutputStream => OutputStream): Double = { 
		val out = new ByteArrayOutputStream()
		val objOut = new ObjectOutputStream(wrapper(out))
		objOut.writeObject(ints)
		objOut.flush
		objOut.close
		out.toByteArray.length.toDouble	  
	}
	
	def noop(out: OutputStream) = out
	
	def compressorAround(out: OutputStream) : OutputStream
}