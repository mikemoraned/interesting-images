package com.houseofmoran.interestingimages

import javax.ws.rs._
import javax.ws.rs.core._
import javax.imageio._

import java.io._

import java.net.URI

import java.util.logging._
import java.awt.image.BufferedImage

import org.apache.commons.io.output.ThresholdingOutputStream
import org.apache.commons.io.IOUtils

@Path("/")
class InterestingImagePartsResource {

  val logger = Logger.getLogger(this.getClass.getName)

  val IMAGE_SIZE_LIMIT = 1024 * 1024 * 10;
  
  val gzip = new GzipStreamEstimator
  val jpeg = new JpegSizeEstimator
  val imageTransformers : Map[String,Function[BufferedImage,BufferedImage]] = 
    Map("raw"              -> Interesting.noop,
	"static"           -> Interesting.grid(gzip) _,
	"static-gzip"      -> Interesting.grid(gzip) _,
	"static-jpeg"      -> Interesting.grid(jpeg) _,
	"nested"           -> Interesting.nested(gzip) _,
	"nested-gzip"      -> Interesting.nested(gzip) _,
	"nested-jpeg"      -> Interesting.nested(jpeg) _,
	"nested-jpegvgzip" -> Interesting.nestedComparison(jpeg, gzip) _)
  
  val imageAnimators : Map[String,Function[BufferedImage,Array[Byte]]] =       
    Map("animated"              -> Interesting.animated(gzip),
	"animated-gzip"         -> Interesting.animated(gzip),
	"animated-jpeg"         -> Interesting.animated(jpeg))
  
  @GET
  @Path("/{spec: [a-z-]+}/{uri: .+}")
  @Produces(Array("image/png"))
  def transformToImage(@PathParam("spec") spec: String, @PathParam("uri") uri: URI) = {
    logger.info("uri: " + uri)
    imageTransformers.get(spec) match {
      case None => throw new WebApplicationException(Response.Status.NOT_FOUND)
      case Some(transformer) => {
    	val out = new ByteArrayOutputStream
    	ImageIO.write(transformer(loadImage(uri)), "png", out)
    	out.toByteArray
      }
    }
  }

  @GET
  @Path("/{spec: animated[a-z-]*}/{uri: .+}")
  @Produces(Array("image/gif"))
  def animate(@PathParam("spec") spec: String, @PathParam("uri") uri: URI) = {
    logger.info("uri: " + uri)
    imageAnimators.get(spec) match {
      case None => throw new WebApplicationException(Response.Status.NOT_FOUND)
      case Some(animator) => {
    	animator(loadImage(uri))
      }
    }
  }
  
  def loadImage(uri: URI) = {
    try {
      ImageIO.read(loadBytes(uri, IMAGE_SIZE_LIMIT))
    }
    catch {
      case e: IIOException => {
	logger.log(Level.WARNING, "Couldn't load: " + e)
	throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
	 	 			  .`type`(MediaType.TEXT_PLAIN_TYPE)
	 	 			  .entity("Can't find " + uri).build)
      }
    }
  }

  def loadBytes(uri: URI, sizeLimit: Int) = {
    val in = uri.toURL.openStream();
    try {
      val bout = new ByteArrayOutputStream();
      IOUtils.copy(in, new ThresholdingOutputStream(sizeLimit) {
	def getStream() = bout
	def thresholdReached() = throw new ByteLimitReachedIOException(sizeLimit);
      });
      new ByteArrayInputStream(bout.toByteArray())
    }
    catch {
      case e: ByteLimitReachedIOException => {
	logger.log(Level.WARNING, "Couldn't load: " + e)
	throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
	 	 			  .`type`(MediaType.TEXT_PLAIN_TYPE)
	 	 			  .entity("Image at uri is too large: " + uri).build)
      }
    }
    finally {
      IOUtils.closeQuietly(in)
    }
  }
}
