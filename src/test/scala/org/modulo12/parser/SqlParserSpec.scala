package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class SqlParserSpec extends AnyFlatSpec with should.Matchers with Inside {

  "parser" should "should return empty no midi files in non existent directory" in {
    val result = SqlParser.parse("select midi from resources/nonexistent;")
    result should equal(List())
  }

  "parser" should "should return midi files in directory as is if no where clause is present" in {
    val result = SqlParser.parse("select midi from resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }

  "parser" should "should return midi files in directory with different case of keywords" in {
    val result = SqlParser.parse("SELECT MIDI FROM resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }
}

