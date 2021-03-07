package org.modulo12.sql

import org.modulo12.{ Modulo12Parser, Modulo12ParserBaseVisitor }

import collection.JavaConverters._
import org.modulo12.core.{
  Comparator,
  DirectoryToAnalyze,
  FileType,
  FileTypesToAnalye,
  LogicalExpression,
  LogicalOperator,
  RequestedBarLinesComparison,
  RequestedInstrumentType,
  RequestedLyrics,
  RequestedScaleType,
  RequestedTempoComparison,
  ScaleType,
  SimpleExpression,
  Song,
  SongMetadataEvaluator,
  SongsSatisfyingQuery,
  SongsToAnalyze,
  SqlSubQueryResult,
  UnknownSimpleExpression,
  WhereExpression
}
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser

import java.io.File
import java.util

class SqlVisitor(midiParser: MidiParser, musicXMLParser: MusicXMLParser)
    extends Modulo12ParserBaseVisitor[SqlSubQueryResult] {
  // This is the top level for visitor
  override def visitSql_statement(ctx: Modulo12Parser.Sql_statementContext): SongsSatisfyingQuery = {
    val fileTypesToAnalye  = visitInput_list_clause(ctx.input_list_clause()).fileTypes
    val directoryToAnalyze = visitFrom_clause(ctx.from_clause()).directory
    val allSongsToAnalyze  = acquireSongsForProcessing(fileTypesToAnalye, directoryToAnalyze)
    val songsSatisfyingQuery =
      if (ctx.where_clause() != null)
        evaluateWhereClause(ctx, allSongsToAnalyze)
      else
        allSongsToAnalyze
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

  override def visitWhere_clause(ctx: Modulo12Parser.Where_clauseContext): WhereExpression = {
    // TODO: Add support for conditionals like AND/NOT/OR etc
    val expression = ctx.expression()
    constructLogicalExpression(expression)
  }

  override def visitSimple_expression(ctx: Modulo12Parser.Simple_expressionContext): SimpleExpression =
    // TODO: Add other simple expressions and expand the visitor using pattern matching
    if (ctx.scale_type() != null) {
      val requestedScaleTypeStr = ctx.scale_type().SCALE_TYPE().getText
      val requestedScaleType    = ScaleType.fromString(requestedScaleTypeStr)
      RequestedScaleType(requestedScaleType)
    } else if (ctx.song_has_instrument() != null) {
      val requestedInstrument = ctx.song_has_instrument().ID().getText
      RequestedInstrumentType(requestedInstrument)
    } else if (ctx.tempo_comparison() != null) {
      val comparator = Comparator.fromString(ctx.tempo_comparison().relational_op().getText)
      val tempo      = ctx.tempo_comparison().NUMBER().getText.toDouble
      RequestedTempoComparison(tempo, comparator)
    } else if (ctx.lyrics_comparison() != null) {
      val lyricsToCompare = ctx.lyrics_comparison().words().word().asScala.map(_.getText)
      RequestedLyrics(lyricsToCompare.toList)
    } else if (ctx.num_barlines_comparsion() != null) {
      val comparator  = Comparator.fromString(ctx.num_barlines_comparsion().relational_op().getText)
      val numBarLines = ctx.num_barlines_comparsion().NUMBER().getText.toDouble
      RequestedBarLinesComparison(numBarLines, comparator)
    } else
      UnknownSimpleExpression

  private def evaluateWhereClause(
      ctx: Modulo12Parser.Sql_statementContext,
      allSongsToAnalyze: List[Song]
  ): List[Song] = {
    val whereExpr = visitWhere_clause(ctx.where_clause())
    evaluateWhereExpression(whereExpr, allSongsToAnalyze)
  }

  private def evaluateWhereExpression(w: WhereExpression, allSongsToAnalyze:List[Song]): List[Song] =
    w match {
      case s: SimpleExpression  => evaluateSimpleExpression(s, allSongsToAnalyze)
      case l: LogicalExpression => evaluateLogicalExpression(l, allSongsToAnalyze)
    }

  private def evaluateLogicalExpression(l: LogicalExpression, allSongsToAnalyze: List[Song]): List[Song] = {
    val leftExpr = evaluateWhereExpression(l.leftExpr, allSongsToAnalyze)
    val rightExpr = evaluateWhereExpression(l.rightExpr, allSongsToAnalyze)

    l.logicalOperator match {
      case LogicalOperator.AND => leftExpr.toSet.intersect(rightExpr.toSet).toList
      case LogicalOperator.OR => leftExpr.toSet.union(rightExpr.toSet).toList
    }
  }

  private def evaluateSimpleExpression(s: SimpleExpression, allSongsToAnalyze:List[Song]): List[Song] =
    s match {
      case RequestedScaleType(scaleType) =>
        SongMetadataEvaluator.filtersSongsWithScaleType(scaleType, allSongsToAnalyze)
      case RequestedInstrumentType(instrument) =>
        SongMetadataEvaluator.filterSongsWithInstrument(instrument, allSongsToAnalyze)
      case RequestedTempoComparison(tempo, comparator) =>
        SongMetadataEvaluator.filterSongsWithTempoComparsion(tempo, comparator, allSongsToAnalyze)
      case RequestedBarLinesComparison(numBarlines, comparator) =>
        SongMetadataEvaluator.filterSongsWithNumBarsComparsion(numBarlines, comparator, allSongsToAnalyze)
      case RequestedLyrics(lyrics) =>
        SongMetadataEvaluator.filterSongWithLyrics(lyrics, allSongsToAnalyze)
      case UnknownSimpleExpression => allSongsToAnalyze
    }

  def constructLogicalExpression(
     expression: Modulo12Parser.ExpressionContext
  ): WhereExpression = {
    if (expression.simple_expression() != null) {
      visitSimple_expression(expression.simple_expression())
    } else {
      val exprLeft   = expression.expression(0)
      val exprRight  = expression.expression(1)
      val evalExprLeft =
        if (exprLeft.simple_expression() != null) constructLogicalExpression(exprLeft)
        else visitSimple_expression(exprLeft.simple_expression())
      val evalExprRight =
        if (exprRight.simple_expression() != null) constructLogicalExpression(exprRight)
        else visitSimple_expression(exprRight.simple_expression())

      val logicalOp  = LogicalOperator.fromString(expression.logical_op().getText)
      LogicalExpression(evalExprLeft, logicalOp, evalExprRight)
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
