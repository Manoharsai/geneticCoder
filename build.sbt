lazy val root = (project in file(".")).
  settings(
    name := "enigma",
    version := "0.1",
    scalaVersion := "2.11.4",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default"
  )
