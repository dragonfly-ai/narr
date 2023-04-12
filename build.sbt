val appVersion:String = "0.101"
val globalScalaVersion = "3.2.1"

ThisBuild / tlBaseVersion := "0.1" // your current series x.y

ThisBuild / organization := "ai.dragonfly"
ThisBuild / organizationName := "dragonfly.ai"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List( tlGitHubDev("dragonfly-ai", "dragonfly.ai") )
ThisBuild / scalaVersion := globalScalaVersion
r
ThisBuild / tlBaseVersion := appVersion
ThisBuild / tlCiReleaseBranches := Seq()
ThisBuild / tlSonatypeUseLegacyHost := false

lazy val narr = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .settings(
    name := "narr",
    description := "Native Typed Arrays with Scala Semantics for Scala JVM, ScalaJS, and Scala Native!"
  )
  .jvmSettings()
  .jsSettings()

lazy val demo = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .enablePlugins(NoPublishPlugin)
  .dependsOn(narr)
  .settings(
    name := "demo",
    Compile / mainClass := Some("Demo"),
    libraryDependencies ++= Seq( "ai.dragonfly" %%% "democrossy" % "0.101" )
  ).jsSettings(
    Compile / fastOptJS / artifactPath := file("./docs/js/main.js"),
    Compile / fullOptJS / artifactPath := file("./docs/js/main.js"),
    scalaJSUseMainModuleInitializer := true
  )
  .jvmSettings()


lazy val root = tlCrossRootProject.aggregate(narr).settings(name := "narr")

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin).settings(
  mdocVariables := Map(
    "VERSION" -> appVersion,
    "SCALA_VERSION" -> globalScalaVersion
  ),
  laikaConfig ~= { _.withRawContent }
)

lazy val unidocs = project
  .in(file("unidocs"))
  .enablePlugins(TypelevelUnidocPlugin) // also enables the ScalaUnidocPlugin
  .settings(
    name := "narr-docs",
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(narr.jvm, narr.js, narr.native)
  )