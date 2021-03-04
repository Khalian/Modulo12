package org.modulo12.musicxml

import org.jfugue.integration.MusicXmlParser
import org.jfugue.midi.MidiFileManager
import nu.xom.ParsingException
import org.modulo12.core.{
  FileType,
  MusicFileParser,
  ParseFileResult,
  Song,
  SongData,
  SongMetadata,
  SongParserListener
}

import java.io.File
import javax.sound.midi.InvalidMidiDataException

class MusicXMLParser extends MusicFileParser {
  private val musicXMLParser = new MusicXmlParser

  override def parseFile(musicXmlFile: File): ParseFileResult =
    if (musicXmlFile.exists())
      try {
        val listener = new SongParserListener
        musicXMLParser.addParserListener(listener)
        musicXMLParser.parse(musicXmlFile)
        val (songMeta, songData) = extractSongDataFromListener(listener)
        ParseFileResult.Success(Song(musicXmlFile.getName, songMeta, songData))
      } catch {
        case e: ParsingException => {
          ParseFileResult.IncorrectFileType(musicXmlFile.getAbsolutePath, FileType.MusicXML)
        }
      }
    else
      ParseFileResult.FileNotFound(musicXmlFile.getAbsolutePath)

  override def fileExtensions(): List[String] =
    List("xml")
}
