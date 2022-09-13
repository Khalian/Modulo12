package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main
import org.modulo12.sql.SqlParser

class BinaryLogicalOperationSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "return nothing for a valid AND condition with one of the statements as false" in {
    Main.sqlEval("select midi from resources where tempo = 120 AND song has lyrics friday, go;") should equal(List())
  }

  it should "return song correctly for a valid AND condition with both statements as true" in {
    Main.sqlEval("select midi from resources where tempo = 120 and song has instrument piano;") should equal(
      List("MIDI_sample.mid")
    )
  }

  it should "return nothing for a valid AND condition with both statements as false" in {
    Main.sqlEval("select midi from resources where tempo = 110 AND song has instrument guitar;") should equal(List())
  }

  it should "return song correctly for a valid OR condition with both statements true" in {
    Main.sqlEval("select midi from resources where tempo = 120 OR song has instrument piano;") should equal(
      List("MIDI_sample.mid")
    )
  }

  it should "return song correctly for a valid OR condition with one of the statements as true" in {
    Main.sqlEval("select midi from resources where tempo = 120 OR song has lyrics friday, go;") should equal(
      List("MIDI_sample.mid")
    )
  }

  it should "return nothing for a valid OR condition with both statements as false" in {
    Main.sqlEval("select midi from resources where tempo = 150 OR song has lyrics friday, go;") should equal(List())
  }

  // TODO: Write more complex unit tests here
}
