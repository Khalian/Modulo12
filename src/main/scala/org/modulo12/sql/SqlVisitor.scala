package org.modulo12.sql

import org.modulo12.core.models._
import org.modulo12.midi.MidiParser
import org.modulo12.musicxml.MusicXMLParser
import org.modulo12.{ Modulo12Parser, Modulo12ParserBaseVisitor }

import java.io.File
import java.util
import scala.collection.JavaConverters._

class SqlVisitor() extends Modulo12ParserBaseVisitor[SqlSubQuery] {
  // This is the top level for visitor
  override def visitSql_statement(ctx: Modulo12Parser.Sql_statementContext): SqlAST = {
    val selectExpression = SelectExpression(visitInput_list_clause(ctx.input_list_clause()))
    val fromExpression   = FromExpression(visitFrom_clause(ctx.from_clause()))
    val whereExpression =
      if (ctx.where_clause() != null)
        Option(visitWhere_clause(ctx.where_clause()))
      else
        None
    SqlAST(selectExpression, fromExpression, whereExpression)
  }

  override def visitInput_list_clause(ctx: Modulo12Parser.Input_list_clauseContext): FileTypesToAnalye = {
    val fileTypes = ctx
      .input_name()
      .asScala
      .flatMap(fileType =>
        fileType.getText.toUpperCase match {
          case "MIDI"     => List(FileType.Midi)
          case "MUSICXML" => List(FileType.MusicXML)
          case "*"        => List(FileType.Midi, FileType.MusicXML)
        }
      )
    FileTypesToAnalye(fileTypes.toSet)
  }

  override def visitFrom_clause(ctx: Modulo12Parser.From_clauseContext): DirectoryToAnalyze =
    DirectoryToAnalyze(new File(ctx.directory_name().ID().getText))

  override def visitWhere_clause(ctx: Modulo12Parser.Where_clauseContext): WhereExpression = {
    val expression = ctx.expression()
    constructLogicalExpression(expression)
  }

  override def visitSimple_expression(ctx: Modulo12Parser.Simple_expressionContext): SimpleExpression =
    // TODO: Add other simple expressions and expand the visitor using pattern matching
    if (ctx.scale_comparison() != null) {
      val requestedScaleTypeStr = ctx.scale_comparison().SCALE_TYPE().getText
      val requestedScaleType    = Scale.fromString(requestedScaleTypeStr)
      RequestedScaleType(requestedScaleType)
    } else if (ctx.key_comparison() != null) {
      val requestedKeyTypeStr = ctx.key_comparison().key_type().getText
      val requestedKeyType    = Key.fromString(requestedKeyTypeStr)
      RequestedKeyType(requestedKeyType)
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
    } else if (ctx.num_barlines_comparision() != null) {
      val comparator  = Comparator.fromString(ctx.num_barlines_comparision().relational_op().getText)
      val numBarLines = ctx.num_barlines_comparision().NUMBER().getText.toDouble
      RequestedBarLinesComparison(numBarLines, comparator)
    } else if (ctx.num_tracks_comparision() != null) {
      val comparator = Comparator.fromString(ctx.num_tracks_comparision().relational_op().getText)
      val numTracks  = ctx.num_tracks_comparision().NUMBER().getText.toDouble
      RequestedTracksComparison(numTracks, comparator)
    } else
      UnknownSimpleExpression

  def constructLogicalExpression(
      expression: Modulo12Parser.ExpressionContext
  ): WhereExpression =
    if (expression.simple_expression() != null)
      visitSimple_expression(expression.simple_expression())
    else {
      val exprLeft  = expression.expression(0)
      val exprRight = expression.expression(1)
      val evalExprLeft =
        if (exprLeft.simple_expression() != null) constructLogicalExpression(exprLeft)
        else visitSimple_expression(exprLeft.simple_expression())
      val evalExprRight =
        if (exprRight.simple_expression() != null) constructLogicalExpression(exprRight)
        else visitSimple_expression(exprRight.simple_expression())

      val logicalOp = LogicalOperator.fromString(expression.logical_op().getText)
      LogicalExpression(evalExprLeft, logicalOp, evalExprRight)
    }
}
