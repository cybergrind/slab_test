libraryDependencies += "org.monifu" %% "monifu" % "1.0"

val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.6" % Test

scalacOptions ++= Seq("-unchecked", "-deprecation")

val commonSettings = Seq(
  organization := "slab_test",
  version := "0.1",
  scalaVersion := "2.11.7",
  fork in test := true,
  libraryDependencies += scalatest
)

lazy val root = Project("root", file("."))
  .settings(commonSettings: _*)

