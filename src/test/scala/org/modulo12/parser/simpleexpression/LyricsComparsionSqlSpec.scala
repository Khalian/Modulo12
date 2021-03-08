package org.modulo12.parser.simpleexpression

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class LyricsComparsionSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql parser" should "return song correctly if lyric word requested is in the song" in {
    SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics something;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if lyric words requested is in the song" in {
    SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics something, heil;") should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing if lyrics requested are not in the song" in {
    SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics wrong, words;") should equal(List())
  }

  it should "return nothing if lyrics requested but there are no lyrics in the song" in {
    SqlParser.parse("SELECT midi FROM resources where SONG has lyrics something;") should equal(List())
  }
}