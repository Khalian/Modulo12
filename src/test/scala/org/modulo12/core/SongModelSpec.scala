package org.modulo12.core

import org.scalatest._
import matchers._
import flatspec._
import org.modulo12.midi.MidiParser

import java.io.File

class SongModelSpec extends AnyFlatSpec with should.Matchers {
  "ScaleType toString" should "evaluate minor scale correctly" in {
    Scale.fromString("minor") should be(Scale.MINOR)
    Scale.fromString("Minor") should be(Scale.MINOR)
    Scale.fromString("MINOR") should be(Scale.MINOR)
    Scale.fromString("MiNoR") should be(Scale.MINOR)
  }
  
  it should "evaluate major scale correctly" in {
    Scale.fromString("major") should be(Scale.MAJOR)
    Scale.fromString("Major") should be(Scale.MAJOR)
    Scale.fromString("MAJOR") should be(Scale.MAJOR)
    Scale.fromString("MaJoR") should be(Scale.MAJOR)
  }
  
  it should "evaluate unknown scale correctly" in {
    Scale.fromString("unknown") should be(Scale.UNKNOWN)
    Scale.fromString("UNknown") should be(Scale.UNKNOWN)
    Scale.fromString("UNKNOWN") should be(Scale.UNKNOWN)
  }

  it should "evaluate to unknown on invalid inputs" in {
    Scale.fromString("") should be(Scale.UNKNOWN)
    Scale.fromString("blah") should be(Scale.UNKNOWN)
    Scale.fromString("gibberish") should be(Scale.UNKNOWN)
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
  
  "Logical Operator from string" should "evaluate AND operator correctly" in {
    LogicalOperator.fromString("AND") should be (LogicalOperator.AND)
    LogicalOperator.fromString("and") should be (LogicalOperator.AND)
    LogicalOperator.fromString("aND") should be (LogicalOperator.AND)
  }
  
  it should "evaluate OR operator correctly" in {
    LogicalOperator.fromString("OR") should be (LogicalOperator.OR)
    LogicalOperator.fromString("or") should be (LogicalOperator.OR)
    LogicalOperator.fromString("Or") should be (LogicalOperator.OR)
  }
  
  "Key rotateOnCircle of Fifth" should "evaluate correctly for keys" in {
    Key.rotateOnCircleOfFifths(0) should be (Key.C)
    Key.rotateOnCircleOfFifths(1) should be (Key.G)
    Key.rotateOnCircleOfFifths(2) should be (Key.D)
    Key.rotateOnCircleOfFifths(3) should be (Key.A)
    Key.rotateOnCircleOfFifths(4) should be (Key.E)
    Key.rotateOnCircleOfFifths(5) should be (Key.B)
    Key.rotateOnCircleOfFifths(6) should be (Key.Fshrp)
    Key.rotateOnCircleOfFifths(7) should be (Key.Cshrp)
    Key.rotateOnCircleOfFifths(-1) should be (Key.F)
    Key.rotateOnCircleOfFifths(-2) should be (Key.Bb)
    Key.rotateOnCircleOfFifths(-3) should be (Key.Eb)
    Key.rotateOnCircleOfFifths(-4) should be (Key.Ab)
    Key.rotateOnCircleOfFifths(-5) should be (Key.Db)
    Key.rotateOnCircleOfFifths(-6) should be (Key.Gb)
    Key.rotateOnCircleOfFifths(-7) should be (Key.Cb)
  }
  
  "Key fromString" should "evaluate correctly for keys" in {
    Key.fromString("c") should be (Key.C)
    Key.fromString("G") should be (Key.G)
    Key.fromString("d") should be (Key.D)
    Key.fromString("A") should be (Key.A)
    Key.fromString("e") should be (Key.E)
    Key.fromString("b") should be (Key.B)
    Key.fromString("f#") should be (Key.Fshrp)
    Key.fromString("C#") should be (Key.Cshrp)
    Key.fromString("F") should be (Key.F)
    Key.fromString("Bb") should be (Key.Bb)
    Key.fromString("eb") should be (Key.Eb)
    Key.fromString("AB") should be (Key.Ab)
    Key.fromString("Db") should be (Key.Db)
    Key.fromString("Gb") should be (Key.Gb)
    Key.fromString("Cb") should be (Key.Cb)
  }
}