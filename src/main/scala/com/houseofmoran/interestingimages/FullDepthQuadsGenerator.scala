package com.houseofmoran.interestingimages

import java.awt.image.BufferedImage

class FullDepthQuadsGenerator(maxDepth: Int) extends InterestingStructureFinder[QuadTree[Double]] {
  def findInterestingStructure(image: BufferedImage) : QuadTree[Double] = {
    generate(0)	
  }
  
  def generate(depth: Int) : QuadTree[Double] = {
    if (depth == maxDepth) {
      QuadLeaf(0.0)
    } else {
      QuadInner(0.0, generate(depth+1), generate(depth+1), generate(depth+1), generate(depth+1) )
    }
  }
}
