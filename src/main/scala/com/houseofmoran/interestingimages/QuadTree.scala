package com.houseofmoran.interestingimages

import java.awt.geom.Rectangle2D

abstract class QuadTree[A](val annotation: A) {
  def quadsWithLevel : Seq[(QuadTree[A],Int)] = {
    quadsWithLevelFromDepth(0)
  }
  
  def annotatedRectanglesByLevel(rootRect: Rectangle2D) : Map[Int,Seq[(Rectangle2D,A)]] = {
    annotatedRectanglesWithLevelFromDepth(rootRect, 0).groupBy(_._2).mapValues( s => s.map(_._1 ) )
  }
	
  def annotate[B](rootRect: Rectangle2D)(annotator: Rectangle2D => B) : QuadTree[B] = {
    annotateFromDepth(rootRect, 0)(annotator)
  }
  
  def quadsWithLevelFromDepth(depth: Int) : Seq[(QuadTree[A],Int)]
  
  def annotatedRectanglesWithLevelFromDepth(parentRect: Rectangle2D, depth: Int) : Seq[((Rectangle2D,A),Int)]
  
  def annotateFromDepth[B](parentRect: Rectangle2D, depth: Int)(annotator: Rectangle2D => B) : QuadTree[B]
}

case class QuadInner[A](override val annotation: A, tl: QuadTree[A], tr: QuadTree[A], bl: QuadTree[A], br: QuadTree[A]) extends QuadTree[A](annotation) {
  def quadsWithLevelFromDepth(depth: Int) : Seq[(QuadTree[A],Int)] = {
    (Seq((this.asInstanceOf[QuadTree[A]],depth)) /: children){ 
      (l, c) =>
	l ++ c.quadsWithLevelFromDepth(depth+1)
    }
  }
	
  val childrenWithOffsets = 
    Seq((tl,(0,0)), (tr,(0,1)), (bl,(1,0)), (br, (1,1)))
  
  def children = childrenWithOffsets.map(_._1)
	
  def annotatedRectanglesWithLevelFromDepth(parentRect: Rectangle2D, depth: Int) : Seq[((Rectangle2D,A),Int)] = {
    val parentWidth = parentRect.getWidth().toFloat
    val parentHeight = parentRect.getHeight().toFloat
    val width = parentWidth / 2.0f
    val height = parentHeight / 2.0f
    (Seq(((parentRect,annotation),depth)) /: childrenWithOffsets) { 
      case (l, (child, (xOff,yOff))) => 
	val rect = new Rectangle2D.Float(parentRect.getX.toFloat + (xOff * width), 
					 parentRect.getY.toFloat + (yOff * height), width, height)
      l ++ child.annotatedRectanglesWithLevelFromDepth(rect, depth + 1)
    }
  }
	
  def annotateFromDepth[B](parentRect: Rectangle2D, depth: Int)(annotator: Rectangle2D => B) : QuadTree[B] = {
    val parentWidth = parentRect.getWidth().toFloat
    val parentHeight = parentRect.getHeight().toFloat
    val width = parentWidth / 2.0f
    val height = parentHeight / 2.0f
    val annotatedChildren = childrenWithOffsets.map { 
      case (child, (xOff, yOff)) => 
	val rect = new Rectangle2D.Float(parentRect.getX.toFloat + (xOff * width), parentRect.getY.toFloat + (yOff * height), width, height)
      child.annotateFromDepth(rect, depth+1)(annotator)
    }
    QuadInner(annotator(parentRect), annotatedChildren(0), annotatedChildren(1), annotatedChildren(2), annotatedChildren(3))
  }
}

case class QuadLeaf[A](override val annotation: A) extends QuadTree[A](annotation) {
  def quadsWithLevelFromDepth(depth: Int) : Seq[(QuadTree[A],Int)] = Seq((this,depth))
  def annotatedRectanglesWithLevelFromDepth(parentRect: Rectangle2D, depth: Int) = Seq(((parentRect,annotation),depth))
  def annotateFromDepth[B](parentRect: Rectangle2D, depth: Int)(annotator: Rectangle2D => B) : QuadTree[B] = QuadLeaf(annotator(parentRect))
}
