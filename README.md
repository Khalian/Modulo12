# Modulo12
Sql Query Engine for Midi and Music XML files

[![Build Status](https://travis-ci.org/Khalian/Modulo12.svg?branch=master)](https://travis-ci.org/Khalian/Modulo12) [![Join the chat at https://gitter.im/Khalian/Modulo12](https://badges.gitter.im/Khalian/Modulo12.svg)](https://gitter.im/Khalian/Modulo12?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Development commands

```
# Clean up dependency cache
sbt clean

# SBT Compile
sbt compile

# Run tests
sbt test

# Format files
sbt scalafmt

# This command launches VS Code for development. 
# I am not proscribing using VSCode, but dotty and sbt supports it natively.
sbt launchIDE

# SBT repl server (once you start this you can just write test instead of full sbt and it will run in repl)
sbt

# SBT debug server (Note the 5005 is the remote jvm debug port. You can use your ide to hook to it and then debug tests by running test)
sbt -jvm-debug 5005
```
