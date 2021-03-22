package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main

class SelectClauseSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "return empty for non existent directory" in {
    Main.sqlEval("select midi from resources/nonexistent;") should equal(List())
  }

  it should "return empty for valid direcotry with no midi" in {
    Main.sqlEval("select midi from resources/testmusicxmlfiles;") should equal(List())
  }

  it should "return empty for valid direcotry with no music xml" in {
    Main.sqlEval("select musicxml from resources/testmidifiles;") should equal(List())
  }

  it should "return midi files in directory as is if no where clause is present" in {
    Main.sqlEval("select midi from resources/testmidifiles;") should equal(List("MIDI_sample.mid"))
  }

  it should "return midi files in directory with different case of keywords" in {
    Main.sqlEval("SELECT MIDI FROM resources/testmidifiles;") should equal(List("MIDI_sample.mid"))
  }
  
  it should "return music xml files in directory as is if no where clause is present" in {
    Main.sqlEval("select musicxml from resources/testmusicxmlfiles;") should equal(List("musicXMLTest.xml"))
  }

  it should "return music xml files in directory with different case of keywords" in {
    Main.sqlEval("SELECT MUSICXML FROM resources/testmusicxmlfiles;") should equal(List("musicXMLTest.xml"))
  }

  it should "return both music xml and midi if present under directory" in {
    Main.sqlEval("SELECT MUSICXML, MIDI FROM resources;") should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }

  it should "only return music xml if only queried for music xml" in {
    Main.sqlEval("SELECT MUSICXML FROM resources;") should equal(List("musicXMLTest.xml"))
  }

  it should "only return midi if only queried for midi" in {
    Main.sqlEval("select Midi FROM resources;") should equal(List("MIDI_sample.mid"))
  }
  
  it should "return all files for wildcard after select clause on correct directory" in {
    Main.sqlEval("select * FROM resources;") should equal(List("musicXMLTest.xml", "MIDI_sample.mid"))
  }
  
  it should "return empty for wildcard after select clause on incorrect directory" in {
    Main.sqlEval("select * FROM incorrectDir;") should equal(List())
  }
  
  it should "be valid syntax if there is a number in the directory" in {
    Main.sqlEval("select * FROM incorrectDir123;") should equal(List())
  }
}