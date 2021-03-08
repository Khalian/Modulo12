package org.modulo12.core

object SongMetadataEvaluator {
  def filtersSongsWithScaleType(requestedScaleType: Scale, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      val keySignature = song.metadata.keySignature
      keySignature.map(sig => sig.scale.equals(requestedScaleType)).getOrElse(false)
    }

  def filtersSongsWithKeyType(requestedKeyType: Key, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      val keySignature = song.metadata.keySignature
      keySignature.map(sig => sig.key.equals(requestedKeyType)).getOrElse(false)
    }

  def filterSongsWithInstrument(instrument: String, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.metadata.instrumentNames.map(_.toLowerCase).contains(instrument.toLowerCase)
    }

  def filterSongsWithTempoComparsion(tempo: Double, comparator: Comparator, songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.metadata.temposBPM.exists(songTempo => Comparator.compare(songTempo, comparator, tempo))
    }

  def filterSongsWithNumBarsComparsion(
      numBars: Double,
      comparator: Comparator,
      songsToAnalyze: List[Song]
  ): List[Song] =
    songsToAnalyze.filter { song =>
      Comparator.compare(song.metadata.numBarLines, comparator, numBars)
    }

  def filterSongWithLyrics(lyrics: List[String], songsToAnalyze: List[Song]): List[Song] =
    songsToAnalyze.filter { song =>
      song.data.lyrics match {
        case Some(lyricsInSong) => lyrics.exists(lyric => lyricsInSong.toLowerCase().contains(lyric.toLowerCase()))
        case None               => false
      }
    }
}
