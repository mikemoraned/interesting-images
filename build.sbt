import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

name := "interesting-images"

version := "1.0"

scalaVersion := "2.8.1"

resolvers += "twitter-repo" at "http://maven.twttr.com"

libraryDependencies ++= Seq("com.twitter" % "finagle-core" % "1.9.0", "com.twitter" % "finagle-http" % "1.9.0")

libraryDependencies ++= Seq(
   "org.eclipse.jetty" % "jetty-servlet" % "7.6.2.v20120308",
   "org.eclipse.jetty" % "jetty-webapp" % "7.6.2.v20120308",
   "com.sun.jersey" % "jersey-core" % "1.8",
   "com.sun.jersey" % "jersey-server" % "1.8",
   "com.sun.jersey" % "jersey-json" % "1.8"
)

ivyXML := 
<dependency org="org.eclipse.jetty.orbit" name="javax.servlet" rev="2.5.0.v201103041518">
  <artifact name="javax.servlet" type="orbit" ext="jar"/>
</dependency>