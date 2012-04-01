package com.houseofmoran.wikiblog.site.images

import javax.ws.rs._
import javax.ws.rs.core._
import javax.imageio._

import java.io.ByteArrayOutputStream

import java.net.URI

import java.util.logging._
import java.awt.image.BufferedImage

@Path("/projects/interesting/")
class InterestingImagePartsResource {

  val logger = Logger.getLogger(this.getClass.getName)
  
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
  @Path("image/{spec: [a-z-]+}/{uri: .+}")
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
  @Path("image/{spec: animated[a-z-]*}/{uri: .+}")
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
	 	  ImageIO.read(uri.toURL)
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
}
