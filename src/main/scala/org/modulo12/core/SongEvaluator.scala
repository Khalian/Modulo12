package org.modulo12.core

import org.modulo12.core.models.{
  FileType,
  LogicalExpression,
  LogicalOperator,
  RequestedBarLinesComparison,
  RequestedInstrumentType,
  RequestedKeyType,
  RequestedLyrics,
  RequestedScaleType,
  RequestedTempoComparison,
  RequestedTracksComparison,
  SimpleExpression,
  Song,
  SqlAST,
  UnknownSimpleExpression,
  WhereExpression
}
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser

import java.io.File

class SongEvaluator(midiParser: MidiParser, musicXmlParser: MusicXMLParser) {
  def processSqlQuery(sqlAST: SqlAST): List[Song] = {
    val fileTypes      = sqlAST.selectExpression.fileTypesToAnalye.fileTypes
    val directory      = sqlAST.fromExpression.directoryToAnalyze.directory
    val songsToAnalyze = acquireSongsForProcessing(fileTypes, directory)
    sqlAST.whereExpression.map(expr => evaluateWhereExpression(expr, songsToAnalyze)).getOrElse(songsToAnalyze)
  }

  private def acquireSongsForProcessing(fileTypesToAnalye: Set[FileType], directoryToAnalyze: File): List[Song] = {
    val xmlSongsToAnalyze =
      if (fileTypesToAnalye.contains(FileType.MusicXML))
        musicXmlParser.parseAllFiles(directoryToAnalyze)
      else
        List()

    val midiSongToAnalyze =
      if (fileTypesToAnalye.contains(FileType.Midi))
        midiParser.parseAllFiles(directoryToAnalyze)
      else
        List()

    xmlSongsToAnalyze ++ midiSongToAnalyze
  }

  private def evaluateWhereExpression(w: WhereExpression, allSongsToAnalyze: List[Song]): List[Song] =
    w match {
      case s: SimpleExpression  => evaluateSimpleExpression(s, allSongsToAnalyze)
      case l: LogicalExpression => evaluateLogicalExpression(l, allSongsToAnalyze)
    }

  private def evaluateLogicalExpression(l: LogicalExpression, allSongsToAnalyze: List[Song]): List[Song] = {
    val leftExpr  = evaluateWhereExpression(l.leftExpr, allSongsToAnalyze)
    val rightExpr = evaluateWhereExpression(l.rightExpr, allSongsToAnalyze)

    l.logicalOperator match {
      case LogicalOperator.AND => leftExpr.toSet.intersect(rightExpr.toSet).toList
      case LogicalOperator.OR  => leftExpr.toSet.union(rightExpr.toSet).toList
    }
  }

  private def evaluateSimpleExpression(s: SimpleExpression, allSongsToAnalyze: List[Song]): List[Song] =
    s match {
      case RequestedScaleType(scaleType) =>
        SongMetadataEvaluator.filtersSongsWithScaleType(scaleType, allSongsToAnalyze)
      case RequestedKeyType(keyType) =>
        SongMetadataEvaluator.filtersSongsWithKeyType(keyType, allSongsToAnalyze)
      case RequestedInstrumentType(instrument) =>
        SongMetadataEvaluator.filterSongsWithInstrument(instrument, allSongsToAnalyze)
      case RequestedTempoComparison(tempo, comparator) =>
        SongMetadataEvaluator.filterSongsWithTempoComparsion(tempo, comparator, allSongsToAnalyze)
      case RequestedBarLinesComparison(numBarlines, comparator) =>
        SongMetadataEvaluator.filterSongsWithNumBarsComparsion(numBarlines, comparator, allSongsToAnalyze)
      case RequestedTracksComparison(numTracks, comparator) =>
        SongMetadataEvaluator.filterSongsWithNumTracksComparsion(numTracks, comparator, allSongsToAnalyze)
      case RequestedLyrics(lyrics) =>
        SongMetadataEvaluator.filterSongWithLyrics(lyrics, allSongsToAnalyze)
      case UnknownSimpleExpression => allSongsToAnalyze
    }
}
