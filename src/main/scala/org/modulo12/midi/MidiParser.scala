package org.modulo12.midi

import java.io.File

import javax.sound.midi.{InvalidMidiDataException, Sequence}
import org.jfugue.midi.MidiFileManager

sealed trait MidiParseResult
object MidiParseResult {
  case class FileNotFound(path: String) extends MidiParseResult
  case class NotMidi(path: String) extends MidiParseResult
  case class Success(sequence: Sequence) extends MidiParseResult
}

object MidiParser {
  def parseMidiFile(path: String): MidiParseResult = {
    val file = new File(path)
    if (file.exists()) {
      try {
        val sequence = MidiFileManager.load(file)
        MidiParseResult.Success(sequence)
      } catch {
        case e: InvalidMidiDataException => MidiParseResult.NotMidi(path)
      }
    } else {
      MidiParseResult.FileNotFound(path)
    }
  }
}
