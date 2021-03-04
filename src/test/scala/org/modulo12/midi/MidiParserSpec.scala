package org.modulo12.midi
import org.scalatest._
import flatspec._
import matchers._
import org.modulo12.coremodels.{ KeySignature, ScaleType, TimeSignature }

import java.io.File
import scala.language.implicitConversions

class MidiParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  val midiFile = new File("resources/testmidifiles/MIDI_sample.mid")

  "parse midi file" should "return FileNotFound for a non exitent file" in {
    val path   = "resources/testmidifiles/nonexistent"
    val result = MidiParser.parseMidiFile(new File(path))
    result should matchPattern { case MidiFileParseResult.NotFound(path) => }
  }

  "it" should "Not Midi for files that are not midi" in {
    val path   = "resources/testmidifiles/filefornegativetest"
    val result = MidiParser.parseMidiFile(new File(path))
    result should matchPattern { case MidiFileParseResult.NotMidi(path) => }
  }

  "it" should "Succes for files that are midi" in {
    val result = MidiParser.parseMidiFile(midiFile)
    result should matchPattern { case MidiFileParseResult.Success(_) => }
  }

  "parse midi sequence" should "parse midi sequence correctly for a midi file" in {
    val result = MidiParser.parseMidiFile(midiFile)
    inside(result) {
      case MidiFileParseResult.Success(sequence) =>
        val (songMeta, songData) = MidiParser.parseSongFromMidiSequence(sequence)
        songMeta.timeSig should be(Some(TimeSignature(4, 2)))
        songMeta.instrumentNames should contain allOf ("Electric_Jazz_Guitar", "Piano", "Electric_Bass_Finger")
        songMeta.temposBPM should be(Set(120))
        // TODO: This nees more thorough testing.
        songMeta.keySignature should be(None)

        songData.chords.size should be(0)
      // TODO: Fix this
//        songData.notes.size should be(1651)
    }
  }

  "get midi files under directory" should "return midi files correctly for a valid directory" in {
    val result = MidiParser.getAllMidiFilesUnderDir(new File("resources/testmidifiles")).map(file => file.getName)
    result should equal(List("MIDI_sample.mid"))
  }

  "get midi files under directory" should "return empty list invalid directory" in {
    val result = MidiParser.getAllMidiFilesUnderDir(new File("resources/invalidDirectory")).map(file => file.getName)
    result should equal(List())
  }
}
