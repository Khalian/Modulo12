package org.modulo12.sql

import org.modulo12.{Modulo12Parser, Modulo12ParserBaseVisitor}

import collection.JavaConverters._
import org.modulo12.core.{DirectoryToAnalyze, FileType, FileTypesToAnalye, RequestedInstrumentType, RequestedScaleType, ScaleType, Song, SongMetadataEvaluator, SongsSatisfyingQuery, SongsToAnalyze, SqlSubQueryResult, SimpleExpression}
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser

import java.io.File

class SqlVisitor(midiParser: MidiParser, musicXMLParser: MusicXMLParser)
    extends Modulo12ParserBaseVisitor[SqlSubQueryResult] {
  // This is the top level for visitor
  override def visitSql_statement(ctx: Modulo12Parser.Sql_statementContext): SongsSatisfyingQuery = {
    val fileTypesToAnalye = visitInput_list_clause(ctx.input_list_clause()).fileTypes
    val directoryToAnalyze = visitFrom_clause(ctx.from_clause()).directory
    val allSongsToAnalyze = acquireSongsForProcessing(fileTypesToAnalye, directoryToAnalyze)
    val songsSatisfyingQuery = if (ctx.where_clause() != null) {
      visitWhere_clause(ctx.where_clause()) match {
        case RequestedScaleType(scaleType) => SongMetadataEvaluator.filtersSongsWithScaleType(scaleType, allSongsToAnalyze)
        case RequestedInstrumentType(instrument) => SongMetadataEvaluator.filterSongsWithInstrument(instrument, allSongsToAnalyze)
      }
    } else {
      allSongsToAnalyze
    }
    SongsSatisfyingQuery(songsSatisfyingQuery)
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

  override def visitWhere_clause(ctx: Modulo12Parser.Where_clauseContext): SimpleExpression =
    // TODO: Add support for conditionals like AND/NOT/OR etc
    visitSimple_expression(ctx.simple_expression())

  override def visitSimple_expression(ctx: Modulo12Parser.Simple_expressionContext): SimpleExpression = {
    // TODO: Add other simple expressions and expand the visitor using pattern matching
    if (ctx.scale_type() != null) {
      val requestedScaleTypeStr = ctx.scale_type().SCALE_TYPE().getText
      val requestedScaleType = ScaleType.fromString(requestedScaleTypeStr)
      RequestedScaleType(requestedScaleType)
    } else {
      val requestedInstrument = ctx.song_has_instrument().ID().getText
      RequestedInstrumentType(requestedInstrument)
    }
  }

  private def acquireSongsForProcessing(fileTypesToAnalye: Set[FileType], directoryToAnalyze: File): List[Song] = {
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

    xmlSongsToAnalyze ++ midiSongToAnalyze
  }
}
