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
  }}