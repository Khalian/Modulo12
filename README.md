# Modulo12

[![Build Status](https://github.com/Khalian/Modulo12/workflows/Modulo12%20CI/badge.svg)](https://github.com/Khalian/Modulo12/actions?query=workflow%3A"Modulo12+CI") [![Join the chat at https://gitter.im/Khalian/Modulo12](https://badges.gitter.im/Khalian/Modulo12.svg)](https://gitter.im/Khalian/Modulo12?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


Modulo12 is a novel SQL like language for parsing data and metadata about music (from midi and musicxml files).

The purpose of this language is to query facts about musicxml and midi files based of western music theory (e.g. queries of the form, get all the songs under a directory with a specific key signature or in tempo range) or metadata about the song's specific performance (get all songs that have guitars in it)

The way it works is you put your songs under a directory and then run a query of this form

```
select [input_file_types] from directory_containing_songs where (conditions);
```

Modulo12 will fetch all songs recursively under the directory and apply some conditions and then output the list of songs that match the conditions.

# Wiki

The full [wiki](https://github.com/Khalian/Modulo12/wiki) contains the language spec, philosophy and technical architecture of the project.

# Examples

Some example commands for querying songs would be like

```
select * from directory_of_songs_path where tempo = 120 and song has lyrics friday;
```

```
select midi from directory_of_songs_path where song has instrument piano;
```

```
# NumBarLines is a function for the number of bars the song is subdivided into
select musicxml from directory_of_songs_path where numbarlines < 40;
```

```
select midi from directory_of_songs_path where key = Eb and tempo > 150;
```

# Development

## Pre requisites

Install [SBT](https://www.scala-sbt.org/1.x/docs/Setup.html)

## To clone the project

```
git clone https://github.com/Khalian/Modulo12
```

## For setting up and running sbt targets

```
# Start modulo12 sql repl
sbt run

# Clean up dependency cache
sbt clean

# SBT Compile
sbt compile

# Run tests
sbt test

# Format files
sbt scalafmt

# SBT repl server (once you start this you can just write test instead of full sbt and it will run in repl)
sbt

# SBT debug server (Note the 5005 is the remote jvm debug port. You can use your ide to hook to it and then debug tests by running test)
sbt -jvm-debug 5005
```

##  IDE Setup

### IntelliJ

Install the following plugins for IntelliJ

1. [Scala](https://www.jetbrains.com/help/idea/discover-intellij-idea-for-scala.html)
2. [Antlr](https://plugins.jetbrains.com/plugin/7358-antlr-v4)

You can then setup it up either as a VCS project from github or as a scala project after you clone it out.

### VSCode

```
# This command launches VS Code for development
sbt launchIDE
```

Please also search the VSCode market place to install the ANTLR4 grammar syntax support plugin

### Vim

Vim is not really an IDE but you can use it for editing, you should install metals along with scala vim syntax highlighting

[Metals](https://scalameta.org/metals/docs/editors/vim.html)

[Scala Vim Syntax](https://www.vim.org/scripts/script.php?script_id=3524)
