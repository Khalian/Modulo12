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

enablePlugins(Antlr4Plugin)
antlr4PackageName in Antlr4 := Some("org.modulo12")

// We wish to use the visitor pattern since we are going to use scala pattern matching for writing the compiler
antlr4GenListener in Antlr4 := false
antlr4GenVisitor in Antlr4 := true