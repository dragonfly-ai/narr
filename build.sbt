ThisBuild / scalaVersion := "3.1.0"
ThisBuild / publishTo := Some( Resolver.file( "file",  new File("/var/www/maven") ) )

lazy val narr = crossProject(JSPlatform, JVMPlatform).settings(
  name := "narr",
  version := "0.01",
  organization := "ai.dragonfly.code",
  resolvers += "dragonfly.ai" at "https://code.dragonfly.ai/",
  scalacOptions ++= Seq("-feature","-deprecation"),
  Compile / mainClass := Some("narr.Demo")
).jvmSettings().jsSettings(
  scalaJSUseMainModuleInitializer := true
)

