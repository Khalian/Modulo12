package org.modulo12.midi

import java.io.File

import javax.sound.midi.{ InvalidMidiDataException, Sequence }
import org.jfugue.midi.{ MidiFileManager, MidiParser }
import org.modulo12.coremodels.{ Song, SongData, SongMetadata }

sealed trait MidiFileParseResult
object MidiFileParseResult {
  case class NotFound(path: String)      extends MidiFileParseResult
  case class NotMidi(path: String)       extends MidiFileParseResult
  case class Success(sequence: Sequence) extends MidiFileParseResult
}

object MidiParser {
  val parser = new MidiParser

  def parseMidiFile(path: String): MidiFileParseResult = {
    val file = new File(path)
    if (file.exists())
      try {
        val sequence = MidiFileManager.load(file)
        MidiFileParseResult.Success(sequence)
      } catch {
        case _: InvalidMidiDataException => MidiFileParseResult.NotMidi(path)
      }
    else
      MidiFileParseResult.NotFound(path)
  }

  def parseSongFromMidiSequence(sequence: Sequence): Song = {
    val listener = new SongParserListener
    parser.addParserListener(listener)
    parser.parse(sequence)
    val metadata = SongMetadata(
      listener.temposBPM.toList,
      listener.keySignature,
      listener.timeSignature,
      listener.instrumentNames.toSet
    )
    val data = SongData(listener.notes.toList, listener.chords.toList)
    Song(metadata, data)
  }
}
