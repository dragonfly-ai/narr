ThisBuild / scalaVersion := "3.2.1"
ThisBuild / publishTo := Some( Resolver.file( "file",  new File("/var/www/maven" ) ) )
ThisBuild / resolvers += "ai.dragonfly.code" at "https://code.dragonfly.ai/"
ThisBuild / organization := "ai.dragonfly.code"
ThisBuild / scalacOptions ++= Seq("-feature", "-deprecation", "-explain")

lazy val narr = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .settings(
    name := "narr",
    version := "0.0321"
  )
  .jvmSettings()
  .jsSettings()


lazy val demo = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .dependsOn(narr)
  .settings(
    name := "demo",
    Compile / mainClass := Some("Demo"),
    libraryDependencies ++= Seq( "ai.dragonfly.code" %%% "democrossy" % "0.02" )
  ).jsSettings(
    Compile / fastOptJS / artifactPath := file("./demo/public_html/js/main.js"),
    Compile / fullOptJS / artifactPath := file("./demo/public_html/js/main.js"),
    scalaJSUseMainModuleInitializer := true
  )
  .jvmSettings()
