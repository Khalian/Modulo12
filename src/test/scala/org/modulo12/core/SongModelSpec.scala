package org.modulo12.core

import org.scalatest._
import matchers._
import flatspec._
import org.modulo12.midi.MidiParser

import java.io.File

class SongModelSpec extends AnyFlatSpec with should.Matchers {
  "ScaleType toString" should "evaluate minor scale correctly" in {
    ScaleType.fromString("minor") should be(ScaleType.MINOR)
    ScaleType.fromString("Minor") should be(ScaleType.MINOR)
    ScaleType.fromString("MINOR") should be(ScaleType.MINOR)
    ScaleType.fromString("MiNoR") should be(ScaleType.MINOR)
  }
  
  it should "evaluate major scale correctly" in {
    ScaleType.fromString("major") should be(ScaleType.MAJOR)
    ScaleType.fromString("Major") should be(ScaleType.MAJOR)
    ScaleType.fromString("MAJOR") should be(ScaleType.MAJOR)
    ScaleType.fromString("MaJoR") should be(ScaleType.MAJOR)
  }
  
  it should "evaluate unknown scale correctly" in {
    ScaleType.fromString("unknown") should be(ScaleType.UNKNOWN)
    ScaleType.fromString("UNknown") should be(ScaleType.UNKNOWN)
    ScaleType.fromString("UNKNOWN") should be(ScaleType.UNKNOWN)
  }

  it should "evaluate to unknown on invalid inputs" in {
    ScaleType.fromString("") should be(ScaleType.UNKNOWN)
    ScaleType.fromString("blah") should be(ScaleType.UNKNOWN)
    ScaleType.fromString("gibberish") should be(ScaleType.UNKNOWN)
  }

  "Comparator fromString" should "should evaluate equals correctly" in {
    Comparator.fromString("=") should be(Comparator.EQ)
  }
  
  it should "should evaluate not equal to correctly" in {
    Comparator.fromString("!=") should be(Comparator.NEQ)
  }
  
  it should "should evaluate less than correctly" in {
    Comparator.fromString("<") should be(Comparator.LT)
  }
  
  it should "should evaluate greater than correctly" in {
    Comparator.fromString(">") should be(Comparator.GT)
  }
  
  it should "should evaluate less than or equal to correctly" in {
    Comparator.fromString("<=") should be(Comparator.LEQ)
  }
  
  it should "should evaluate greater than or equal to correctly" in {
    Comparator.fromString(">=") should be(Comparator.GEQ)
  }
  
  "Comparator compare" should "should compare equals correctly" in {
    Comparator.compare(6.0, Comparator.EQ, 6.0) should be(true)
    Comparator.compare(6.0, Comparator.EQ, 7.0) should be(false)
  }
  
  it should "should compare not equals correctly" in {
    Comparator.compare(6.0, Comparator.NEQ, 6.0) should be(false)
    Comparator.compare(6.0, Comparator.NEQ, 7.0) should be(true)
  }
  
  it should "should compare less than correctly" in {
    Comparator.compare(5.0, Comparator.LT, 6.0) should be(true)
    Comparator.compare(6.0, Comparator.LT, 5.0) should be(false)
  }
  
  it should "should compare greater than correctly" in {
    Comparator.compare(5.0, Comparator.GT, 6.0) should be(false)
    Comparator.compare(6.0, Comparator.GT, 5.0) should be(true)
  }
  
  it should "should compare less than or equal to correctly" in {
    Comparator.compare(5.0, Comparator.LEQ, 6.0) should be(true)
    Comparator.compare(6.0, Comparator.LEQ, 5.0) should be(false)
    Comparator.compare(6.0, Comparator.LEQ, 6.0) should be(true)
  }
  
  it should "should compare greater than or equal to correctly" in {
    Comparator.compare(5.0, Comparator.GEQ, 6.0) should be(false)
    Comparator.compare(6.0, Comparator.GEQ, 5.0) should be(true)
    Comparator.compare(6.0, Comparator.GEQ, 6.0) should be(true)
  }
}