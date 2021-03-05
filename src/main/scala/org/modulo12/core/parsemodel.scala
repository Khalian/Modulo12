package org.modulo12.core

import org.modulo12.core.ParseFileResult.Success

import java.io.File
import javax.sound.midi.Sequence

sealed trait FileType
object FileType {
  case object Midi     extends FileType
  case object MusicXML extends FileType
}

sealed trait ParseFileResult
object ParseFileResult {
  case class FileNotFound(path: String)                          extends ParseFileResult
  case class IncorrectFileType(path: String, fileType: FileType) extends ParseFileResult
  case class Success(song: Song)                                 extends ParseFileResult
}

trait MusicFileParser {
  def parseFile(file: File): ParseFileResult

  def fileExtensions(): List[String]

  def parseAllFiles(directory: File): List[Song] = {
    val files = getAllFilesUnderDirectory(directory)
    val songs = files
      .map(file => (file, parseFile(file)))
      .flatMap {
        case (midiFile, song) =>
          song match {
            case Success(song) => List(song)
            case _             => List()
          }
      }
    songs
  }

  // TODO: Ideally we should be checking the magic number of these files,
  //  I will write that code in at a later date. Currently just using extensions
  def getAllFilesUnderDirectory(dir: File): List[File] =
    if (dir.exists && dir.isDirectory)
      dir.listFiles.filter(_.isFile).toList.filter { file =>
        fileExtensions().exists(file.getName.endsWith(_))
      }
    else
      List[File]()

  def extractSongDataFromListener(listener: SongParserListener): (SongMetadata, SongData) = {
    val metadata = SongMetadata(
      listener.temposBPM.toSet,
      listener.keySignature,
      listener.timeSignature,
      listener.instrumentNames.toSet
    )
    val data = SongData(listener.notes.toList.distinct, listener.chords.toList.distinct)
    (metadata, data)
  }
}
