package org.modulo12.core

import org.jfugue.theory.{ Chord, Note }

// Song data
case class SongData(notes: List[Note], chords: List[Chord], lyrics: Option[String])

sealed trait Key
object Key {
  // Key with no flats or sharps
  case object C extends Key

  // Keys with sharps, laid out clockwise on the circle of fifths
  case object G     extends Key
  case object D     extends Key
  case object A     extends Key
  case object E     extends Key
  case object B     extends Key
  case object Fshrp extends Key
  case object Cshrp extends Key

  // Keys with flats
  case object F  extends Key
  case object Bb extends Key
  case object Eb extends Key
  case object Ab extends Key
  case object Db extends Key
  case object Gb extends Key
  case object Cb extends Key

  def rotateOnCircleOfFifths(byAmount: Integer): Key =
    byAmount match {
      case 0  => C
      case 1  => G
      case 2  => D
      case 3  => A
      case 4  => E
      case 5  => B
      case 6  => Fshrp
      case 7  => Cshrp
      case -1 => F
      case -2 => Bb
      case -3 => Eb
      case -4 => Ab
      case -5 => Db
      case -6 => Gb
      case -7 => Cb
    }

  def fromString(value: String): Key =
    value.toUpperCase match {
      case "C"  => C
      case "G"  => G
      case "D"  => D
      case "A"  => A
      case "E"  => E
      case "B"  => B
      case "F#" => Fshrp
      case "C#" => Cshrp
      case "F"  => F
      case "BB" => Bb
      case "EB" => Eb
      case "AB" => Ab
      case "DB" => Db
      case "GB" => Gb
      case "CB" => Cb
    }
}

// Song metadata
sealed trait Scale
object Scale {
  case object MAJOR   extends Scale;
  case object MINOR   extends Scale;
  case object UNKNOWN extends Scale;

  def fromString(value: String): Scale = {
    val valueUpper = value.toUpperCase()
    if (valueUpper.equals("MAJOR"))
      MAJOR
    else if (valueUpper.equals("MINOR"))
      MINOR
    else
      UNKNOWN
  }
}

case class KeySignature(key: Key, scale: Scale)

// ADT representing the metadata and data about a song.
case class TimeSignature(beatsPerMeasure: Int, noteValuePerBeat: Int)

case class SongMetadata(
    numBarLines: Int,
    temposBPM: Set[Int],
    keySignature: Option[KeySignature],
    timeSig: Option[TimeSignature],
    instrumentNames: Set[String]
)

case class Song(filePath: String, metadata: SongMetadata, data: SongData)
