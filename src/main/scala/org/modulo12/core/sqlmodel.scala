package org.modulo12.core

import java.io.File

sealed trait SqlSubQueryResult

sealed trait SimpleExpressionResult extends SqlSubQueryResult

// Intermediate and final set of songs (Should I converge the two types?)
case class SongsToAnalyze(songs: List[Song])       extends SqlSubQueryResult
case class SongsSatisfyingQuery(songs: List[Song]) extends SqlSubQueryResult

// From clause sub results
case class DirectoryToAnalyze(directory: File)         extends SqlSubQueryResult
case class FileTypesToAnalye(fileTypes: Set[FileType]) extends SqlSubQueryResult

// Where clause sub results
case class RequestedScaleType(scaleType: ScaleType) extends SqlSubQueryResult
