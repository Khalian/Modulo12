package org.modulo12.coremodels

// TODO: Decouple from jfugue's theory models, we should be able to swap jfugue later on if we have to
import org.jfugue.theory.{ Chord, Note }

// Song data
case class SongData(notes: List[Note], chords: List[Chord])

// Song metadata
sealed trait ScaleType
object ScaleType {
  case object MAJOR   extends ScaleType;
  case object MINOR   extends ScaleType;
  case object UNKNOWN extends ScaleType;
}

case class KeySignature(key: Integer, scaleType: ScaleType)

// ADT representing the metadata and data about a song.
case class TimeSignature(beatsPerMeasure: Int, noteValuePerBeat: Int)

case class SongMetadata(
    temposBPM: List[Int],
    keySignature: Option[KeySignature],
    timeSig: Option[TimeSignature],
    instrumentNames: Set[String]
)

case class Song(metadata: SongMetadata, data: SongData)
