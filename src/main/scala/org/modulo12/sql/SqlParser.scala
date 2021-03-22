package org.modulo12.sql

import org.antlr.v4.runtime.{ ANTLRInputStream, CharStreams, CommonTokenStream }
import org.modulo12.core.models.SqlAST
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser
import org.modulo12.{ Modulo12Lexer, Modulo12Parser }

object SqlParser {
  def parse(query: String): SqlAST = {
    val charStream = CharStreams.fromString(query)
    val lexer      = new Modulo12Lexer(charStream)
    val tokens     = new CommonTokenStream(lexer)
    val parser     = new Modulo12Parser(tokens)

    // By default antlr injects a console listener, and the way that worked is that it posted
    // syntax errors onto console and then proceeded to actually try to parse the query.
    // This bit of code removes that console parser and instead puts a new one that stops execution on
    // syntax errors
    parser.removeErrorListeners()
    parser.addErrorListener(new SqlParserErrorListener())

    new SqlVisitor().visitSql_statement(parser.sql_statement())
  }
}
