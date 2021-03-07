package org.modulo12.core

import org.jfugue.theory.{ Chord, Note }

// Song data
case class SongData(notes: List[Note], chords: List[Chord], lyrics: Option[String])

// Song metadata
sealed trait ScaleType
object ScaleType {
  case object MAJOR   extends ScaleType;
  case object MINOR   extends ScaleType;
  case object UNKNOWN extends ScaleType;

  def fromString(value: String): ScaleType = {
    val valueUpper = value.toUpperCase()
    if (valueUpper.equals("MAJOR"))
      MAJOR
    else if (valueUpper.equals("MINOR"))
      MINOR
    else
      UNKNOWN
  }
}

case class KeySignature(key: Integer, scaleType: ScaleType)

// ADT representing the metadata and data about a song.
case class TimeSignature(beatsPerMeasure: Int, noteValuePerBeat: Int)

case class SongMetadata(
    temposBPM: Set[Int],
    keySignature: Option[KeySignature],
    timeSig: Option[TimeSignature],
    instrumentNames: Set[String]
)

case class Song(filePath: String, metadata: SongMetadata, data: SongData)
