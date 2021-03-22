package org.modulo12.core.models

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
