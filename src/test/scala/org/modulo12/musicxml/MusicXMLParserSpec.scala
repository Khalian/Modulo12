package org.modulo12.musicxml

import org.scalatest._
import matchers._
import flatspec._
import org.modulo12.core.ScaleType.MAJOR
import org.modulo12.core.{FileType, KeySignature, ParseFileResult, ScaleType, Song, TimeSignature}

import java.io.File
import scala.language.implicitConversions

class MusicXMLParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  val musicXMLFile = new File("resources/testmusicxmlfiles/musicXMLTest.xml")
  val musicXmlParser = new MusicXMLParser

  "parse midi file" should "return FileNotFound for a non exitent file" in {
    val path   = "resources/testmusicxmlfiles/nonexistent"
    val result = musicXmlParser.parseFile(new File(path))
    result should matchPattern { case ParseFileResult.FileNotFound(path) => }
  }

  it should "return incorrect file type for files that are not music xml" in {
    val path   = "resources/testmusicxmlfiles/filefornegativetest"
    val result = musicXmlParser.parseFile(new File(path))
    result should matchPattern { case ParseFileResult.IncorrectFileType(path, FileType.MusicXML) => }
  }

  "get music xml files under directory" should "return midi files correctly for a valid directory" in {
    val result = musicXmlParser.getAllFilesUnderDirectory(new File("resources/testmusicxmlfiles")).map(file => file.getName)
    result should equal(List("musicXMLTest.xml"))
  }

  "parse file" should "parse music xml file correctly for a correct file" in {
    val result = musicXmlParser.parseFile(musicXMLFile)
    inside(result) {
      // TODO: Verify and Improve these tests
      case ParseFileResult.Success(song) =>
        val Song(songFileName, songMeta, songData) = song
        songMeta.timeSig should be(None)
        songMeta.instrumentNames should be(Set())
        songMeta.temposBPM should be(Set())
        songMeta.keySignature should contain(KeySignature(-3, MAJOR))
        songMeta.numBarLines should be(2)

        songData.chords.size should be(0)
        songData.notes.size should be(6)
        songData.lyrics should contain("something du nicht, heil ger")
    }
  }
  it should "return empty list invalid directory" in {
    val result = musicXmlParser.getAllFilesUnderDirectory(new File("resources/invalidDirectory")).map(file => file.getName)
    result should equal(List())
  }
}
