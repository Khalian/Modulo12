package org.modulo12.midi
import org.scalatest._
import flatspec._
import matchers._
import scala.language.implicitConversions

class MidiParserSpec extends AnyFlatSpec with should.Matchers {
  "parse midi file" should "return FileNotFound for a non exitent file" in {
    val path = "resources/testmidifiles/nonexistent"
    val result = MidiParser.parseMidiFile(path)
    result should matchPattern { case MidiParseResult.FileNotFound(path) => }
  }

  "it" should "Not Midi for files that are not midi" in {
    val path = "resources/testmidifiles/filefornegativetest"
    val result = MidiParser.parseMidiFile(path)
    result should matchPattern { case MidiParseResult.NotMidi(path) => }
  }

  "it" should "Succes for files that are midi" in {
    val path = "resources/testmidifiles/MIDI_sample.mid"
    val result = MidiParser.parseMidiFile(path)
    result should matchPattern { case MidiParseResult.Success(_) => }
  }
}
