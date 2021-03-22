package org.modulo12.sql

import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.{ ANTLRErrorListener, Parser, RecognitionException, Recognizer }
import org.modulo12.core.models.InvalidQueryException

import java.util

class SqlParserErrorListener extends ANTLRErrorListener {
  override def reportAmbiguity(
      recognizer: Parser,
      dfa: DFA,
      startIndex: Int,
      stopIndex: Int,
      exact: Boolean,
      ambigAlts: util.BitSet,
      configs: ATNConfigSet
  ): Unit = ???

  override def reportAttemptingFullContext(
      recognizer: Parser,
      dfa: DFA,
      startIndex: Int,
      stopIndex: Int,
      conflictingAlts: util.BitSet,
      configs: ATNConfigSet
  ): Unit = ???

  override def syntaxError(
      recognizer: Recognizer[_, _],
      offendingSymbol: Any,
      line: Int,
      charPositionInLine: Int,
      msg: String,
      e: RecognitionException
  ): Unit =
    throw new InvalidQueryException(s"$msg")

  override def reportContextSensitivity(
      recognizer: Parser,
      dfa: DFA,
      startIndex: Int,
      stopIndex: Int,
      prediction: Int,
      configs: ATNConfigSet
  ): Unit = ???
}
