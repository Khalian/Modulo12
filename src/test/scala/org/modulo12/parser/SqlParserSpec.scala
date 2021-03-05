package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class SqlParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  "parser" should "should return empty for non existent directory" in {
    val result = SqlParser.parse("select midi from resources/nonexistent;")
    result should equal(List())
  }
  
  it should "should return empty for valid direcotry with no midi" in {
    val result = SqlParser.parse("select midi from resources/testmusicxmlfiles;")
    result should equal(List())
  }

  it should "should return empty for valid direcotry with no music xml" in {
    val result = SqlParser.parse("select musicxml from resources/testmidifiles;")
    result should equal(List())
  }  
  
  it should "should return midi files in directory as is if no where clause is present" in {
    val result = SqlParser.parse("select midi from resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "should return midi files in directory with different case of keywords" in {
    val result = SqlParser.parse("SELECT MIDI FROM resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "should return music xml files in directory as is if no where clause is present" in {
    val result = SqlParser.parse("select musicxml from resources/testmusicxmlfiles;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "should return music xml files in directory with different case of keywords" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources/testmusicxmlfiles;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "should return both music xml and midi if present under directory" in {
    val result = SqlParser.parse("SELECT MUSICXML, MIDI FROM resources;")
    result should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }

  it should "should only return music xml if only queried for music xml" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "should only return midi if only queried for midi" in {
    val result = SqlParser.parse("select Midi FROM resources;")
    result should equal(List("MIDI_sample.mid"))
  }
}