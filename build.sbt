ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

enablePlugins(ScalaJSPlugin)

// the project is configured to use ScalaJS and Laminar
lazy val root = project
  .in(file("."))
  .settings(
    name := "laminar-scalajs",
    libraryDependencies ++= Seq(
      "com.raquo"    %%% "laminar"     % "16.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    ),
  )
