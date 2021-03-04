package org.modulo12.sql

import org.modulo12.{Modulo12Parser, Modulo12ParserBaseVisitor}
import org.modulo12.core.{ScaleOfSong, ScaleType, SimpleExpressionResult, Song, SongsSatisfyingQuery, SongsToAnalyze, SqlSubQueryResult}
import org.modulo12.midi.MidiParser

import java.io.File

class SqlVisitor(parser: MidiParser) extends Modulo12ParserBaseVisitor[SqlSubQueryResult] {
  // This is the top level for visitor
  override def visitSql_statement(ctx: Modulo12Parser.Sql_statementContext): SongsSatisfyingQuery = {
    val songsToAnalyze         = visitFrom_clause(ctx.from_clause())
    SongsSatisfyingQuery(songsToAnalyze.songs)
  }

  override def visitFrom_clause(ctx: Modulo12Parser.From_clauseContext): SongsToAnalyze = {
    val directory = new File(ctx.directory_name().ID().getText)
    val allFiles = parser.parseAllFiles(directory)
    SongsToAnalyze(allFiles)
  }
}
