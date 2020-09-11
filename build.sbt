val dottyVersion = "0.27.0-RC1"
val scala213Version = "2.13.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Modulo12",
    version := "0.1.0",

    // Extra Maven repositories
    resolvers += "JFugue Repository" at "https://maven.sing-group.org/repository/maven/",

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test",
    libraryDependencies += "jfugue" % "jfugue" % "5.0.9",

    // To make the default compiler and REPL use Dotty
    scalaVersion := dottyVersion,

    // To cross compile with Dotty and Scala 2
    crossScalaVersions := Seq(dottyVersion, scala213Version)
  )
