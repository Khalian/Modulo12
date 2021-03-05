package org.modulo12.sql

import org.modulo12.{ Modulo12Parser, Modulo12ParserBaseVisitor }

import collection.JavaConverters._
import org.modulo12.core.{
  DirectoryToAnalyze,
  FileType,
  FileTypesToAnalye,
  ScaleType,
  SimpleExpressionResult,
  Song,
  SongsSatisfyingQuery,
  SongsToAnalyze,
  SqlSubQueryResult
}
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser

import java.io.File

class SqlVisitor(midiParser: MidiParser, musicXMLParser: MusicXMLParser)
    extends Modulo12ParserBaseVisitor[SqlSubQueryResult] {
  // This is the top level for visitor
  override def visitSql_statement(ctx: Modulo12Parser.Sql_statementContext): SongsSatisfyingQuery = {
    val fileTypesToAnalye  = visitInput_list_clause(ctx.input_list_clause()).fileTypes
    val directoryToAnalyze = visitFrom_clause(ctx.from_clause()).directory

    val xmlSongsToAnalyze =
      if (fileTypesToAnalye.contains(FileType.MusicXML))
        musicXMLParser.parseAllFiles(directoryToAnalyze)
      else
        List()

    val midiSongToAnalyze =
      if (fileTypesToAnalye.contains(FileType.Midi))
        midiParser.parseAllFiles(directoryToAnalyze)
      else
        List()

    val allSongs = xmlSongsToAnalyze ++ midiSongToAnalyze
    // TODO: Implement where conditionals here
    SongsSatisfyingQuery(allSongs)
  }

  override def visitInput_list_clause(ctx: Modulo12Parser.Input_list_clauseContext): FileTypesToAnalye = {
    val fileTypes = ctx
      .input_name()
      .asScala
      .map(fileType =>
        fileType.getText.toUpperCase match {
          case "MIDI"     => FileType.Midi
          case "MUSICXML" => FileType.MusicXML
        }
      )
    FileTypesToAnalye(fileTypes.toSet)
  }

  override def visitFrom_clause(ctx: Modulo12Parser.From_clauseContext): DirectoryToAnalyze =
    DirectoryToAnalyze(new File(ctx.directory_name().ID().getText))
}
