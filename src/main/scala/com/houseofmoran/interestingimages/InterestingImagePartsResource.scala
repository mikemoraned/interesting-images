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
  @Path("/compressability/{uri: .+}")
  @Produces(Array("text/html"))
  def all(@PathParam("uri") uri: URI) = {
    <html>
    <head>
    <title>'Interesting' parts of {uri}</title>
    <script type="text/javascript">
    <xml:unparsed>
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-30965307-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
    </xml:unparsed>
</script>
    </head>
    <body>
    <h1>{uri}</h1>
    <p>The images and animations below illustrate how a simple compressor at the image or byte level (e.g. jpeg or gzip) can be used to highlight the most 'interesting' parts of an image. All very simplistic, I know, but I wanted to see how well it could capture the information content of a image.</p>
    <h2>Images</h2>
    <p>The overlayed annuli below visualise the compressability of an image block. The inner radius represents the minimum compressed size of <em>any</em> block, whilst the outer radius is the compressed size of the block it is over.</p>
    { for (name <- imageTransformers.keys.toList.sorted) yield { <h3>{name}</h3><img src={"/" + name + "/" + uri} title={name} /> } }
    <h2>Animations</h2>
    <p>The animations show each block in reverse order of compressability i.e. least-complex first.</p>
    { for (name <- imageAnimators.keys.toList.sorted) yield { <h3>{name}</h3><img src={"/" + name + "/" + uri} title={name} /> } }
    </body>
    </html>.toString()
  }

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
