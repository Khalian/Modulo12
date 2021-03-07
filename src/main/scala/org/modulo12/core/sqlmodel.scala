package org.modulo12.core

import java.io.File

sealed trait SqlSubQueryResult

// Intermediate and final set of songs (Should I converge the two types?)
case class SongsToAnalyze(songs: List[Song])       extends SqlSubQueryResult
case class SongsSatisfyingQuery(songs: List[Song]) extends SqlSubQueryResult

// From clause sub results
case class DirectoryToAnalyze(directory: File)         extends SqlSubQueryResult
case class FileTypesToAnalye(fileTypes: Set[FileType]) extends SqlSubQueryResult

// Where clause sub results
sealed trait WhereExpression extends SqlSubQueryResult
case class LogicalExpression(
    leftExpr: WhereExpression,
    logicalOperator: LogicalOperator,
    rightExpr: WhereExpression
) extends WhereExpression
sealed trait SimpleExpression                                                     extends WhereExpression
case class RequestedScaleType(scaleType: ScaleType)                               extends SimpleExpression
case class RequestedInstrumentType(instrument: String)                            extends SimpleExpression
case class RequestedTempoComparison(tempo: Double, operator: Comparator)          extends SimpleExpression
case class RequestedBarLinesComparison(numBarlines: Double, operator: Comparator) extends SimpleExpression
case class RequestedLyrics(words: List[String])                                   extends SimpleExpression
case object UnknownSimpleExpression                                               extends SimpleExpression

sealed trait Comparator

object Comparator {
  case object EQ  extends Comparator // Equals
  case object NEQ extends Comparator // Not Equals
  case object GEQ extends Comparator // Greater than or equals
  case object LEQ extends Comparator // Less than or equals
  case object GT  extends Comparator // Greater than
  case object LT  extends Comparator // Less than

  def fromString(value: String): Comparator =
    value match {
      case ">=" => GEQ
      case "!=" => NEQ
      case "<=" => LEQ
      case "="  => EQ
      case "<"  => LT
      case ">"  => GT
    }

  def compare(operand1: Double, comparator: Comparator, operand2: Double): Boolean =
    comparator match {
      case EQ  => operand1 == operand2
      case GEQ => operand1 >= operand2
      case LEQ => operand1 <= operand2
      case LT  => operand1 < operand2
      case GT  => operand1 > operand2
      case NEQ => operand1 != operand2
    }
}

sealed trait LogicalOperator
case object LogicalOperator {
  case object AND extends LogicalOperator
  case object OR  extends LogicalOperator

  def fromString(value: String): LogicalOperator =
    value.toUpperCase match {
      case "AND" => AND
      case "OR"  => OR
    }
}
