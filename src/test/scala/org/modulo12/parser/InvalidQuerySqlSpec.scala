package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main
import org.modulo12.core.models.InvalidQueryException
import org.modulo12.sql.SqlParser

class InvalidQuerySqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "throw invalid query exception for wrong select clause" in {
    the [InvalidQueryException] thrownBy(Main.sqlEval("selec")) should have message "mismatched input 'selec' expecting SELECT"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select wrong")) should have message "mismatched input 'wrong' expecting {MIDI, MUSICXML, '*'}"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi")) should have message "mismatched input '<EOF>' expecting {FROM, ','}"
  }
  
  it should "throw invalid query exception for the wrong from clause" in {
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi fro")) should have message "missing FROM at 'fro'"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from")) should have message "mismatched input '<EOF>' expecting {FROM, ','}"
  }
  
  it should "throw invalid query exception for the wrong where clause" in {
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where")) should have message "mismatched input '<EOF>' expecting {KEY, SCALE, SONG, TEMPO, NUMBARLINES, NUMTRACKS}"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where INVALIDENTITY > 666")) should have message "mismatched input 'INVALIDENTITY' expecting {KEY, SCALE, SONG, TEMPO, NUMBARLINES, NUMTRACKS}"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where tempo <> 120")) should have message "extraneous input '>' expecting NUMBER"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where numbarlines ? 120")) should have message "missing {'=', '<=', '>=', '!=', '<', '>'} at '120'"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where song has got lyrics hello")) should have message "no viable alternative at input 'songhasgot'"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where tempo > 120 XOR numbarlines = 2")) should have message "mismatched input 'XOR' expecting {AND, OR, ';'}"
    the [InvalidQueryException] thrownBy(Main.sqlEval("select midi from folder where tempo > 120 AND numbarlines = 2")) should have message "missing ';' at '<EOF>'"
  }
}
