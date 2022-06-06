ThisBuild / scalaVersion := "3.1.0"
ThisBuild / publishTo := Some( Resolver.file( "file",  new File("/var/www/maven") ) )

lazy val arraybridge = crossProject(JSPlatform, JVMPlatform).settings(
  name := "arraybridge",
  version := "0.01",
  organization := "ai.dragonfly.code",
  resolvers += "dragonfly.ai" at "https://code.dragonfly.ai/",
  scalacOptions ++= Seq("-feature","-deprecation"),
  Compile / mainClass := Some("arraybridge.Demo")
).jvmSettings(

).jsSettings(
  scalaJSUseMainModuleInitializer := true
)

