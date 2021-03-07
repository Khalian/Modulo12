package org.modulo12.core

object SongMetadataEvaluator {
  def filtersSongsWithScaleType(requestedScaleType: ScaleType, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      val keySignature = song.metadata.keySignature
      keySignature.map(sig => sig.scaleType.equals(requestedScaleType)).getOrElse(false)
    }

  def filterSongsWithInstrument(instrument: String, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.metadata.instrumentNames.map(_.toLowerCase).contains(instrument.toLowerCase)
    }

  def filterSongsWithTempoComparsion(tempo: Double, comparator: Comparator, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.metadata.temposBPM.exists(songTempo => Comparator.compare(songTempo, comparator, tempo))
    }

  def filterSongWithLyrics(lyrics: List[String], songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.data.lyrics match {
        case Some(lyricsInSong) => lyrics.exists(lyric => lyricsInSong.toLowerCase().contains(lyric.toLowerCase()))
        case None               => false
      }
    }
}
