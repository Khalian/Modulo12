package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main

class KeySignatureSqlSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "return song correctly where condition for scale type requested is actual scale type" in {
    Main.sqlEval("SELECT MUSICXML FROM resources where scale = major;") should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing where condition for scale type requested is incorrect" in {
    Main.sqlEval("SELECT MUSICXML FROM resources where scale = minor;") should equal(List())
  }

  it should "return song correctly where condition for key type requested is actual key type" in {
    Main.sqlEval("SELECT musicxml FROM resources where key = Eb;") should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing where condition for key type requested is incorrect" in {
    Main.sqlEval("SELECT MUSICXML FROM resources where key = Bb;") should equal(List())
    Main.sqlEval("SELECT MUSICXML FROM resources where key = F#;") should equal(List())
    Main.sqlEval("SELECT MUSICXML FROM resources where key = C;") should equal(List())
  }

  it should "return song correctly if request for key signature is correct" in {
    Main.sqlEval("SELECT MUSICXML FROM resources where key = Eb AND scale = major;") should equal(
      List("musicXMLTest.xml")
    )
    Main.sqlEval("SELECT MUSICXML FROM resources where key = Eb OR scale = major;") should equal(
      List("musicXMLTest.xml")
    )
  }

  it should "return song correctly if request for key signature is incorrect" in {
    Main.sqlEval("SELECT MUSICXML FROM resources where key = Db AND scale = minor;") should equal(List())
    Main.sqlEval("SELECT MUSICXML FROM resources where key = Bb OR scale = minor;") should equal(List())
  }

  // TODO: Add more test cases
}
