package com.houseofmoran.wikiblog.site.images

import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.io.ObjectOutputStream
import java.io.OutputStream

class BestSpeedDeflaterEstimator extends ArrayCompressedSizeEstimator {

  def compressorAround(out: OutputStream) : OutputStream = {
	  val deflater = new Deflater(Deflater.BEST_SPEED)
	  new DeflaterOutputStream(out, deflater)
  }
}