package org.modulo12

import org.jline.reader.{ EndOfFileException, LineReaderBuilder, UserInterruptException }
import org.jline.terminal.{ Terminal, TerminalBuilder }
import org.jline.utils.{ AttributedStringBuilder, AttributedStyle }
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
      val songsSatisfyingQuery = SqlParser.parse(query)
      songsSatisfyingQuery.foreach(song => println(song))
    } match {
      case Success(_)                         =>
      case Failure(t: UserInterruptException) => exit() // Ctrl C
      case Failure(t: EndOfFileException)     => exit() // Ctrl D
      case Failure(t)                         => t.printStackTrace()
    }

    repl()
  }
}
