package org.modulo12.parser.simpleexpression

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main
import org.modulo12.sql.SqlParser

class TempoComparisonSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql parser" should "return song correctly if song tempo is greater than requested tempo" in {
    Main.sqlEval("SELECT midi FROM resources where tempo > 90;") should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song tempo is less than requested tempo" in {
    Main.sqlEval("SELECT midi FROM resources where tempo < 150;") should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song tempo is equal to requested tempo" in {
    Main.sqlEval("SELECT midi FROM resources where tempo = 120;") should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song tempo is greater than or equal to requested tempo" in {
    Main.sqlEval("SELECT midi FROM resources where tempo >= 120;") should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song tempo is less than or equal to requested tempo" in {
    Main.sqlEval("SELECT midi FROM resources where tempo <= 120;") should equal(List("MIDI_sample.mid"))
  }

  it should "return nothing if song tempo requested is greater than its value" in {
    Main.sqlEval("SELECT midi FROM resources where tempo > 130;") should equal(List())
  }

  it should "return nothing if song tempo requested is less than its value" in {
    Main.sqlEval("SELECT midi FROM resources where tempo < 110;") should equal(List())
  }

  it should "return nothing if song tempo requested with not equals comparator actually is the requested tempo value" in {
    Main.sqlEval("SELECT midi FROM resources where tempo != 120;") should equal(List())
  }

  it should "return nothing if song tempo requested is greater or equal to than its value" in {
    Main.sqlEval("SELECT midi FROM resources where tempo >= 130;") should equal(List())
  }

  it should "return nothing if song tempo requested is less than or equal to its value" in {
    Main.sqlEval("SELECT midi FROM resources where tempo < 110;") should equal(List())
  }

  it should "return nothing if we try to compare temp and there is no tempo in the song" in {
    Main.sqlEval("SELECT musicxml FROM resources where tempo = 110;") should equal(List())
  }
}