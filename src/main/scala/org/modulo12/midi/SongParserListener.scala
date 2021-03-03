package org.modulo12.midi
import org.jfugue.midi.MidiDictionary
import org.jfugue.parser.ParserListenerAdapter
import org.jfugue.theory.{ Chord, Note }
import org.modulo12.coremodels.{ KeySignature, ScaleType, TimeSignature }

import scala.collection.mutable

class SongParserListener extends ParserListenerAdapter {
  var temposBPM       = new mutable.ListBuffer[Int]()
  var instrumentNames = new mutable.HashSet[String]()
  var timeSignature   = Option.empty[TimeSignature]
  var keySignature    = Option.empty[KeySignature]
  var notes           = new mutable.ListBuffer[Note]
  var chords          = new mutable.ListBuffer[Chord]

  override def onNoteParsed(note: Note): Unit =
    notes.addOne(note)

  override def onChordParsed(chord: Chord): Unit =
    chords.addOne(chord)

  override def onKeySignatureParsed(key: Byte, scale: Byte): Unit = {
    val scaleType = scale.toInt match {
      case 0 => ScaleType.MAJOR
      case 1 => ScaleType.MINOR
      case _ => ScaleType.UNKNOWN
    }

    keySignature = Option(KeySignature(key.toInt, scaleType))
  }

  override def onTempoChanged(tempoBPM: Int): Unit =
    temposBPM.addOne(tempoBPM)

  override def onInstrumentParsed(instrument: Byte): Unit = {
    val instrumentName = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get(instrument)
    instrumentNames.add(instrumentName)
  }
  
  override def onTimeSignatureParsed(numerator: Byte, powerOfTwo: Byte): Unit =
    timeSignature = Option(TimeSignature(numerator.toInt, powerOfTwo.toInt))
}
