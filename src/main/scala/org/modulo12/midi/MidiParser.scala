package org.modulo12.midi

import java.io.File
import javax.sound.midi.{ InvalidMidiDataException, Sequence }
import org.jfugue.midi.{ MidiFileManager, MidiParser => JFugueMidiParser }
import org.modulo12.core.models.{ FileType, ParseFileResult, Song, SongData, SongMetadata }
import org.modulo12.core.{ MusicFileParser, SongParserListener }
import org.modulo12.core.models.ParseFileResult.Success

class MidiParser extends MusicFileParser {
  private val parser = new JFugueMidiParser

  override def parseFile(midiFile: File): ParseFileResult =
    if (midiFile.exists())
      try {
        val sequence             = MidiFileManager.load(midiFile)
        val (songMeta, songData) = parseSongFromMidiSequence(sequence)
        ParseFileResult.Success(Song(midiFile.getName, songMeta, songData))
      } catch {
        case _: InvalidMidiDataException => ParseFileResult.IncorrectFileType(midiFile.getAbsolutePath, FileType.Midi)
      }
    else
      ParseFileResult.FileNotFound(midiFile.getAbsolutePath)

  override def fileExtensions() = List(".mid")

  def parseSongFromMidiSequence(sequence: Sequence): (SongMetadata, SongData) = {
    val listener = new SongParserListener
    parser.addParserListener(listener)
    parser.parse(sequence)
    extractSongDataFromListener(listener)
  }
}
