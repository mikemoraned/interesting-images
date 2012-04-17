package com.houseofmoran.util.servlet

import javax.servlet._
import javax.servlet.http._

import java.awt.GraphicsEnvironment

import java.util.logging._

class RunHeadless extends ServletContextListener
{
  val log = Logger.getLogger(this.getClass.getName)

  def contextInitialized(event: ServletContextEvent)
  {
    log.info("Attempting to run headless")
    System.setProperty("java.awt.headless", "true")
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val headless = ge.isHeadlessInstance()
    log.info("Running headless? " + headless)
  }

  def contextDestroyed(event: ServletContextEvent)
  {
    
  }
}
