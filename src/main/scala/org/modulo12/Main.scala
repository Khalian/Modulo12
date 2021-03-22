package org.modulo12

import org.jline.reader.{ EndOfFileException, LineReaderBuilder, UserInterruptException }
import org.jline.terminal.{ Terminal, TerminalBuilder }
import org.jline.utils.{ AttributedStringBuilder, AttributedStyle }
import org.modulo12.core.SongEvaluator
import org.modulo12.core.models.{ InvalidQueryException, Song }
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser
import org.modulo12.sql.SqlParser

import scala.annotation.tailrec
import scala.sys.exit
import scala.util.{ Failure, Success, Try }

object Main {
  val PROMPT = new AttributedStringBuilder()
    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
    .append("modulo12-sql> ")
    .toAnsi()
  val TERMINAL = TerminalBuilder.builder.build()
  TERMINAL.handle(Terminal.Signal.INT, Terminal.SignalHandler.SIG_IGN)
  val READER = LineReaderBuilder.builder().terminal(TERMINAL).build();

  def main(args: Array[String]): Unit =
    repl()

  @tailrec
  private def repl(): Unit = {
    Try {
      val query                = READER.readLine(PROMPT)
      val songsSatisfyingQuery = sqlEval(query)
      songsSatisfyingQuery.foreach(song => println(song))
    } match {
      case Success(_)                          =>
      case Failure(InvalidQueryException(msg)) => println(msg)
      case Failure(t: UserInterruptException)  => exit() // Ctrl C
      case Failure(t: EndOfFileException)      => exit() // Ctrl D
      case Failure(t)                          => t.printStackTrace()
    }

    repl()
  }

  def sqlEval(query: String): List[String] = {
    val sqlAST               = SqlParser.parse(query)
    val midiParser           = new MidiParser
    val musicXmlParser       = new MusicXMLParser
    val songsSatisfyingQuery = new SongEvaluator(midiParser, musicXmlParser).processSqlQuery(sqlAST)
    songsSatisfyingQuery.map(song => song.filePath)
  }
}
