package org.modulo12.core.models

import org.modulo12.core.models.{
  Comparator,
  DirectoryToAnalyze,
  LogicalOperator,
  SimpleExpression,
  SqlSubQuery,
  WhereExpression,
  _
}

import java.io.File

case class InvalidQueryException(msg: String) extends Exception(msg)

// Top level trait expressing either the query or a sub section of the query
sealed trait SqlSubQuery

// Abstract syntax tree for sql query
case class SqlAST(
    selectExpression: SelectExpression,
    fromExpression: FromExpression,
    whereExpression: Option[WhereExpression]
) extends SqlSubQuery

case class SelectExpression(fileTypesToAnalye: FileTypesToAnalye)
case class FromExpression(directoryToAnalyze: DirectoryToAnalyze)

// From clause sub results
case class DirectoryToAnalyze(directory: File)         extends SqlSubQuery
case class FileTypesToAnalye(fileTypes: Set[FileType]) extends SqlSubQuery

// Where clause sub results
sealed trait WhereExpression extends SqlSubQuery
case class LogicalExpression(
    leftExpr: WhereExpression,
    logicalOperator: LogicalOperator,
    rightExpr: WhereExpression
) extends WhereExpression
sealed trait SimpleExpression                                                     extends WhereExpression
case class RequestedScaleType(scaleType: Scale)                                   extends SimpleExpression
case class RequestedKeyType(key: Key)                                             extends SimpleExpression
case class RequestedInstrumentType(instrument: String)                            extends SimpleExpression
case class RequestedTempoComparison(tempo: Double, operator: Comparator)          extends SimpleExpression
case class RequestedBarLinesComparison(numBarlines: Double, operator: Comparator) extends SimpleExpression
case class RequestedTracksComparison(numTracks: Double, operator: Comparator)     extends SimpleExpression
case class RequestedLyrics(words: List[String])                                   extends SimpleExpression
case object UnknownSimpleExpression                                               extends SimpleExpression
