name := "poker"
version := "0.1"
scalaVersion := "2.13.1"

connectInput in run := true

libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest-propspec" % "3.2.2" % "test"
