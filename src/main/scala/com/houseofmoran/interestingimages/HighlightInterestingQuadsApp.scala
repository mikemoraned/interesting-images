package com.houseofmoran.interestingimages

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage

object HighlightInterestingQuadsApp {
	def main(args : Array[String]) : Unit = {
		val inFile = new File(args(0))
		val outFilename = args(1)
		val outFile = new File(outFilename)
    
		val WithExtension = """.+?\.([^.]+)$""".r
		outFilename match {
			case WithExtension(extension) => 
			{ 
				println(inFile + " -> " + outFile + " (format: " + extension + ")")
				ImageIO.write(Interesting.defaultQuads(ImageIO.read(inFile)), extension, outFile)
			}
			case _ => println("outFile must match " + WithExtension)
		}
	}
}
