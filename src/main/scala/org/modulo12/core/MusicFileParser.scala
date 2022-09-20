package org.modulo12.core

import org.modulo12.core.models.{ ParseFileResult, Song, SongData, SongMetadata }
import org.modulo12.core.models.ParseFileResult.Success

import java.io.File

trait MusicFileParser {
  def parseFile(file: File): ParseFileResult

  def fileExtensions(): List[String]

  def parseAllFiles(directory: File): List[Song] = {
    val files = getAllFilesUnderDirectory(directory)
    val songs = files
      .map(file => (file, parseFile(file)))
      .flatMap { case (midiFile, song) =>
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
    getFileTree(dir)
      .filter(_.isFile)
      .filter(file => fileExtensions().exists(file.getName.endsWith(_)))
      .toList

  protected def extractSongDataFromListener(listener: SongParserListener): (SongMetadata, SongData) = {
    val metadata = models.SongMetadata(
      listener.numBarLines,
      listener.numTracks,
      listener.temposBPM.toSet,
      listener.keySignature,
      listener.timeSignature,
      listener.instrumentNames.toSet
    )
    val lyrics = if (listener.lyrics.isEmpty) None else Option(listener.lyrics.mkString(" "))
    val data =
      SongData(listener.notes.toList.distinct, listener.chords.toList.distinct, lyrics)
    (metadata, data)
  }

  // https://stackoverflow.com/questions/2637643/how-do-i-list-all-files-in-a-subdirectory-in-scala
  private def getFileTree(f: File): LazyList[File] =
    f #:: (if (f.isDirectory) f.listFiles().to(LazyList).flatMap(getFileTree)
           else LazyList.empty)
}
