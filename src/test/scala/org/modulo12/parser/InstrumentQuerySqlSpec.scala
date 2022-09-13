package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main
import org.modulo12.sql.SqlParser

class InstrumentQuerySqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "return song correctly if there is an instrument in it" in {
    Main.sqlEval("SELECT midi FROM resources where song has instrument piano;") should equal(List("MIDI_sample.mid"))
  }

  it should "return nothing if there we query for an instrument and there are none listed in the file" in {
    Main.sqlEval("SELECT musicxml FROM resources where SONG has instrument guitar;") should equal(List())
  }
}
