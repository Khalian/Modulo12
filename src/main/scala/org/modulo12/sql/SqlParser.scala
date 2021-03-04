package org.modulo12.sql

import org.antlr.v4.runtime.{ANTLRInputStream, CharStreams, CommonTokenStream}
import org.modulo12.midi.MidiParser
import org.modulo12.{Modulo12Lexer, Modulo12Parser}

object SqlParser {
  def parse(query: String): List[String] = {
    val charStream = CharStreams.fromString(query)
    val lexer = new Modulo12Lexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new Modulo12Parser(tokens)
    
    val midiParser = new MidiParser
    val songsSatisfyingQuery = new SqlVisitor(midiParser).visitSql_statement(parser.sql_statement())
    songsSatisfyingQuery.songs.map(song => song.filePath)
  }
}
