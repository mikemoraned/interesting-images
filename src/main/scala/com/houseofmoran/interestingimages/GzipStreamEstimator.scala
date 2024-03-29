package com.houseofmoran.interestingimages

import java.io.OutputStream
import java.util.zip.GZIPOutputStream

class GzipStreamEstimator extends ArrayCompressedSizeEstimator {
	def compressorAround(out: OutputStream) : OutputStream = {
	  new GZIPOutputStream(out)
	}
}