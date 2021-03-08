package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class KeySignatureSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql parser" should "return song correctly where condition for scale type requested is actual scale type" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where scale = major;")
    result should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing where condition for scale type requested is incorrect" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where scale = minor;")
    result should equal(List())
  }
  
  it should "return song correctly where condition for key type requested is actual key type" in {
    val result = SqlParser.parse("SELECT musicxml FROM resources where key = Eb;")
    result should equal(List("musicXMLTest.xml"))
  }
  
  it should "return nothing where condition for key type requested is incorrect" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where key = Bb;")
    result should equal(List())
    val result2 = SqlParser.parse("SELECT MUSICXML FROM resources where key = F#;")
    result2 should equal(List())
    val result3 = SqlParser.parse("SELECT MUSICXML FROM resources where key = C;")
    result3 should equal(List())
  }
  
  it should "return song correctly if request for key signature is correct" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where key = Eb AND scale = major;")
    result should equal(List("musicXMLTest.xml"))
    val result2 = SqlParser.parse("SELECT MUSICXML FROM resources where key = Eb OR scale = major;")
    result2 should equal(List("musicXMLTest.xml"))
  }
  
  it should "return song correctly if request for key signature is incorrect" in {
    val result = SqlParser.parse("SELECT MUSICXML FROM resources where key = Db AND scale = minor;")
    result should equal(List())
    val result2 = SqlParser.parse("SELECT MUSICXML FROM resources where key = Bb OR scale = minor;")
    result2 should equal(List())
  }
  
  // TODO: Add more test cases
}
