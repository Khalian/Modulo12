package org.modulo12.parser.simpleexpression

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class NumTracksComparsionSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql parser" should "return song correctly if num tracks queried is equal to the number of tracks in the song" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks = 1;") should equal(List("musicXMLTest.xml"))
    SqlParser.parse("SELECT midi FROM resources where numtracks = 4;") should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song's num of tracks is less than requested num of tracks" in {
    SqlParser.parse("SELECT * FROM resources where numtracks < 6;") should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }

  it should "return song correctly if song's num of tracks is greater than requested num of tracks" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks > 0;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of tracks is not equal to requested num of tracks" in {
    SqlParser.parse("SELECT midi FROM resources where numtracks != 2;") should equal(List("MIDI_sample.mid"))
  }
  
  it should "return song correctly if song's num of tracks is less than or equal to requested num of tracks" in {
    SqlParser.parse("SELECT * FROM resources where numtracks <= 4;") should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }

  it should "return song correctly if song's num of tracks is greater than or equal to requested num of tracks" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks >= 1;") should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing if song num tracks requested is greater than its value" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks > 10;") should equal(List())
  }

  it should "return nothing if song num tracks requested is less than its value" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks < 1;") should equal(List())
  }

  it should "return nothing if song num tracks requested with not equals comparator actually is the num tracks value" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks != 1;") should equal(List())
  }

  it should "return nothing if song num tracks requested is greater or equal to than its value" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks >= 3;") should equal(List())
  }

  it should "return nothing if song num tracks requested is less than or equal to than its value" in {
    SqlParser.parse("SELECT musicxml FROM resources where numtracks <= 0;") should equal(List())
  }
}
