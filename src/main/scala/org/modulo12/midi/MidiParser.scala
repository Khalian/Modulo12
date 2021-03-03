package org.modulo12.midi

import java.io.File
import javax.sound.midi.{InvalidMidiDataException, Sequence}
import org.jfugue.midi.{MidiFileManager, MidiParser}
import org.modulo12.coremodels.{Song, SongData, SongMetadata, SongsToAnalyze}
import org.modulo12.midi.MidiFileParseResult.Success

sealed trait MidiFileParseResult
object MidiFileParseResult {
  case class NotFound(path: String)      extends MidiFileParseResult
  case class NotMidi(path: String)       extends MidiFileParseResult
  case class Success(sequence: Sequence) extends MidiFileParseResult
}

object MidiParser {
  val parser         = new MidiParser
  val midiExtensions = List("mid")
  
  def parseAllFiles(directory: File): List[Song] = {
    val midiFiles = MidiParser.getAllMidiFilesUnderDir(directory)
    val songs = midiFiles
      .map(midiFile => (midiFile, MidiParser.parseMidiFile(midiFile)))
      .flatMap { case (midiFile, song) =>
        song match {
          case Success(sequence) =>
            val (songMeta, songData) = MidiParser.parseSongFromMidiSequence(sequence)
            List(Song(midiFile.getName, songMeta, songData))
          case _ => List()
        }
      }
    songs
  }

  // TODO: Ideally we should be checking the magic number of the midi files, I will write that code in at a later date.
  def getAllMidiFilesUnderDir(dir: File): List[File] =
    if (dir.exists && dir.isDirectory)
      dir.listFiles.filter(_.isFile).toList.filter { file =>
        midiExtensions.exists(file.getName.endsWith(_))
      }
    else
      List[File]()

  def parseMidiFile(midiFile: File): MidiFileParseResult =
    if (midiFile.exists())
      try {
        val sequence = MidiFileManager.load(midiFile)
        MidiFileParseResult.Success(sequence)
      } catch {
        case _: InvalidMidiDataException => MidiFileParseResult.NotMidi(midiFile.getAbsolutePath)
      }
    else
      MidiFileParseResult.NotFound(midiFile.getAbsolutePath)

  def parseSongFromMidiSequence(sequence: Sequence): (SongMetadata, SongData) = {
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
    (metadata, data)
  }
}
