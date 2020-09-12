lazy val root = project
  .in(file("."))
  .settings(
    name := "Modulo12",
    version := "0.1.0",
    // Extra Maven repositories
    resolvers += "JFugue Repository" at "https://maven.sing-group.org/repository/maven/",
    libraryDependencies += "jfugue"         % "jfugue"    % "5.0.9",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.5",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.5" % "test",
    scalaVersion := "3.0.0-RC1"
  )
