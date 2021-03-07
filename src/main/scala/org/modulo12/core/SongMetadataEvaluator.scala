package org.modulo12.core

object SongMetadataEvaluator {
  def filtersSongsWithScaleType(requestedScaleType: ScaleType, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      val keySignature = song.metadata.keySignature
      keySignature.map(sig => sig.scaleType.equals(requestedScaleType)).getOrElse(false)
    }
  
  def filterSongsWithInstrument(instrument: String, songsToAnalyze: List[Song]): List[Song] = {
    songsToAnalyze.filter { song =>
      song.metadata.instrumentNames.map(_.toLowerCase).contains(instrument.toLowerCase)
    }
  }
}
