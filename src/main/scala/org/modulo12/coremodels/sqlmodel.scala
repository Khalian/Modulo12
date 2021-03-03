package org.modulo12.coremodels

sealed trait SqlSubQueryResult

sealed trait SimpleExpressionResult extends SqlSubQueryResult
case class ScaleOfSong(value: String) extends SimpleExpressionResult

case class SongsToAnalyze(songs: List[Song]) extends SqlSubQueryResult
case class SongsSatisfyingQuery(songs: List[Song]) extends SqlSubQueryResult