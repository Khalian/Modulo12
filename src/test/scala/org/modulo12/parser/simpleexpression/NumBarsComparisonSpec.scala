package org.modulo12.parser.simpleexpression

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.Main

class NumBarsComparisonSpec extends AnyFlatSpec with should.Matchers with Inside {
  "sql evaluator" should "return song correctly if num bars queried is equal to the number of bars in the song" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines = 2;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is less than requested num of bars" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines < 3;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is greater than requested num of bars" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines > 1;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is not equal to requested num of bars" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines != 1;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is less than or equal to requested num of bars" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines <= 3;") should equal(List("musicXMLTest.xml"))
  }

  it should "return song correctly if song's num of bars is greater than or equal to than requested num of bars" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines >= 1;") should equal(List("musicXMLTest.xml"))
  }

  it should "return nothing if num bars is not present in the song" in {
    Main.sqlEval("SELECT midi FROM resources where numbarlines = 1;") should equal(List())
  }

  it should "return nothing if song num bar lines requested is greater than its value" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines > 3;") should equal(List())
  }

  it should "return nothing if song num bar lines requested is less than its value" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines < 1;") should equal(List())
  }

  it should "return nothing if song num bar lines requested with not equals comparator actually is the requested value" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines != 2;") should equal(List())
  }

  it should "return nothing if song num bar lines requested is greater or equal to than its value" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines >= 3;") should equal(List())
  }

  it should "return nothing if song num bar lines requested is less than or equal to than its value" in {
    Main.sqlEval("SELECT musicxml FROM resources where numbarlines <= 0;") should equal(List())
  }
}
