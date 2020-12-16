name := "poker"
version := "0.1"
scalaVersion := "2.13.1"

//libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.6-SNAP5" % Test
//libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.15.1" % Test

libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest-propspec" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-matchers-core" % "3.3.0-SNAP3"
libraryDependencies += "org.scalatest" %% "scalatest-shouldmatchers" % "3.3.0-SNAP3" % Test
