package com.houseofmoran.wikiblog.site.images

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage

import java.util.logging.Logger

object PerfTestTargetApp {
	
	val logger = Logger.getLogger(this.getClass.getName)
	
	def main(args : Array[String]) : Unit = {
		val inFile = new File(args(0))
		var runs = 0
		val sampleInterval = 5
		val image = ImageIO.read(inFile)
		var lastSample = System.currentTimeMillis
		while (true) {
			Interesting.defaultStatic(image)
			runs += 1
			if (runs % sampleInterval == 0) {
				val now = System.currentTimeMillis
				val elapsed = now - lastSample
				val timePerRun = elapsed.toDouble / sampleInterval
				lastSample = now
				logger.info("Average time over " + sampleInterval + " runs: " + timePerRun)
			}
		}
	}
}