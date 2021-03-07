package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class SqlParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  "parser" should "return empty for non existent directory" in {
    val result = SqlParser.parse("select midi from resources/nonexistent;")
    result should equal(List())
  }
  
  it should "return empty for valid direcotry with no midi" in {
    val result = SqlParser.parse("select midi from resources/testmusicxmlfiles;")
    result should equal(List())
  }

  it should "return empty for valid direcotry with no music xml" in {
    val result = SqlParser.parse("select musicxml from resources/testmidifiles;")
    result should equal(List())
  }  
  
  it should "return midi files in directory as is if no where clause is present" in {
    val result = SqlParser.parse("select midi from resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return midi files in directory with different case of keywords" in {
    val result = SqlParser.parse("SELECT MIDI FROM resources/testmidifiles;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return music xml files in directory as is if no where clause is present" in {
    val result = SqlParser.parse("select musicxml from resources/testmusicxmlfiles;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "return music xml files in directory with different case of keywords" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources/testmusicxmlfiles;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "return both music xml and midi if present under directory" in {
    val result = SqlParser.parse("SELECT MUSICXML, MIDI FROM resources;")
    result should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }

  it should "only return music xml if only queried for music xml" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "only return midi if only queried for midi" in {
    val result = SqlParser.parse("select Midi FROM resources;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return file correctly where condition for scale type requested is major" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where scale = major;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return nothing where condition for scale type requested is major but song is in minor" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where scale = minor;")
    result should equal(List())
  }
  
  it should "return nothing where condition for scale type requested is unknown but song is in minor" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where scale = minor;")
    result should equal(List())
  }

  it should "return song correctly if there is an instrument in it" in {
    val result = SqlParser.parse("SELECT midi FROM resources where song has instrument piano;")
    result should equal(List("MIDI_sample.mid"))
  }
  
  it should "return nothing if there we query for an instrument and there are none listed in the file" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where SONG has instrument guitar;")
    result should equal(List())
  }

  it should "return song correctly if song tempo is greater than requested tempo" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo > 90;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return song correctly if song tempo is less than requested tempo" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo < 150;")
    result should equal(List("MIDI_sample.mid"))
  }
  
  it should "return song correctly if song tempo is equal to requested tempo" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo = 120;")
    result should equal(List("MIDI_sample.mid"))
  }
  
  it should "return song correctly if song tempo is greater than or equal to requested tempo" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo >= 120;")
    result should equal(List("MIDI_sample.mid"))
  }
  
  it should "return song correctly if song tempo is less than or equal to requested tempo" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo <= 120;")
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return nothing if song tempo requested is greater than its value" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo > 130;")
    result should equal(List())
  }

  it should "return nothing if song tempo requested is less than its value" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo < 110;")
    result should equal(List())
  }

  it should "return nothing if song tempo requested with not equals comparator actually is the requested tempo value" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo != 120;")
    result should equal(List())
  }

  it should "return nothing if song tempo requested is greater or equal to than its value" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo >= 130;")
    result should equal(List())
  }

  it should "return nothing if song tempo requested is less than or equal to its value" in {
    val result = SqlParser.parse("SELECT midi FROM resources where tempo < 110;")
    result should equal(List())
  }
  
  it should "return nothing if we try to compare temp and there is no tempo in the song" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where tempo = 110;")
    result should equal(List())
  }
  
  it should "return song correctly if lyric word requested is in the song" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics something;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if lyric words requested is in the song" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics something, heil;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return nothing if lyrics requested are not in the song" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where SONG has lyrics wrong, words;")
    result should equal(List())
  }
  
  it should "return nothing if lyrics requested but there are no lyrics in the song" in {
    val result = SqlParser.parse("SELECT midi FROM resources where SONG has lyrics something;")
    result should equal(List())
  }
  
  it should "return song correctly if num bars queried is equal to the number of bars in the song" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines = 2;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if song's num of bars is less than requested num of bars" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines < 3;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if song's num of bars is greater than requested num of bars" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines > 1;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if song's num of bars is not equal to requested num of bars" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines != 1;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if song's num of bars is less than or equal to requested num of bars" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines <= 3;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is greater than or equal to than requested num of bars" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines >= 1;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return nothing if num bars is not present in the song" in {
    val result = SqlParser.parse("SELECT midi FROM resources where numbarlines = 1;")
    result should equal(List())
  }
  
  it should "return nothing if song num bar lines requested is greater than its value" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines > 3;")
    result should equal(List())
  }

  it should "return nothing if song num bar lines requested is less than its value" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where numbarlines < 1;")
    result should equal(List())
  }

  it should "return nothing if song num bar lines requested with not equals comparator actually is the requested tempo value" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where tempo != 2;")
    result should equal(List())
  }

  it should "return nothing if song num bar lines requested is greater or equal to than its value" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where tempo >= 3;")
    result should equal(List())
  }

  it should "return nothing if song num bar lines requested is less than or equal to than its value" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where tempo != 2;")
    result should equal(List())
  }
}