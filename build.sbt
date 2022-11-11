ThisBuild / scalaVersion := "3.1.0"
ThisBuild / publishTo := Some( Resolver.file( "file",  new File("/var/www/maven" ) ) )
ThisBuild / resolvers += "ai.dragonfly.code" at "https://code.dragonfly.ai/"
ThisBuild / organization := "ai.dragonfly.code"
ThisBuild / scalacOptions ++= Seq("-feature", "-deprecation", "-explain")

lazy val narr = crossProject(JSPlatform, JVMPlatform).settings(
  name := "narr",
  version := "0.03"
).jvmSettings().jsSettings()


lazy val demo = crossProject(JSPlatform, JVMPlatform).dependsOn(narr).settings(
  name := "demo",
  Compile / mainClass := Some("Demo"),
  libraryDependencies ++= Seq( "ai.dragonfly.code" %%% "democrossy" % "0.0105" )
).jsSettings(
  Compile / fastOptJS / artifactPath := file("./demo/public_html/js/main.js"),
  Compile / fullOptJS / artifactPath := file("./demo/public_html/js/main.js"),
  scalaJSUseMainModuleInitializer := true
).jvmSettings()
