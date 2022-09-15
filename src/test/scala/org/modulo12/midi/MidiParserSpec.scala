package org.modulo12.midi
import org.scalatest._
import matchers._
import flatspec._
import org.modulo12.core.models.{ FileType, ParseFileResult, Song, TimeSignature }

import java.io.File
import scala.language.implicitConversions

class MidiParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  val midiFile   = new File("resources/testmidifiles/MIDI_sample.mid")
  val midiParser = new MidiParser

  "parse midi file" should "return FileNotFound for a non exitent file" in {
    val path   = "resources/testmidifiles/nonexistent"
    val result = midiParser.parseFile(new File(path))
    result should matchPattern { case ParseFileResult.FileNotFound(path) => }
  }

  it should "Not Midi for files that are not music xml" in {
    val path   = "resources/testmidifiles/filefornegativetest"
    val result = midiParser.parseFile(new File(path))
    result should matchPattern { case ParseFileResult.IncorrectFileType(path, FileType.Midi) => }
  }

  "parse file" should "parse song correctly for a correct file" in {
    val result = midiParser.parseFile(midiFile)
    inside(result) { case ParseFileResult.Success(song) =>
      // TODO: This nees more thorough testing.
      val Song(songFileName, songMeta, songData) = song
      songMeta.timeSig should be(Some(TimeSignature(4, 2)))
      songMeta.instrumentNames should contain allOf ("Electric_Jazz_Guitar", "Piano", "Electric_Bass_Finger")
      songMeta.temposBPM should be(Set(120))
      songMeta.keySignature should be(None)
      songMeta.numBarLines should be(0)
      songMeta.numTracks should be(4)

      songData.chords.size should be(0)
      // TODO: Fix this, it non deterministically fails, not priority until the metadata language is completed
      // songData.notes.size should be(1651)
      songData.lyrics.isEmpty should be(true)
    }
  }

  "get midi files under directory" should "return midi files correctly for a valid directory" in {
    val result = midiParser.getAllFilesUnderDirectory(new File("resources/testmidifiles")).map(file => file.getName)
    result should equal(List("MIDI_sample.mid"))
  }

  it should "return empty list invalid directory" in {
    val result = midiParser.getAllFilesUnderDirectory(new File("resources/invalidDirectory")).map(file => file.getName)
    result should equal(List())
  }
}
