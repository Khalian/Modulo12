package org.modulo12.core

import java.io.File

sealed trait SqlSubQueryResult

sealed trait SimpleExpressionResult extends SqlSubQueryResult

case class SongsToAnalyze(songs: List[Song])       extends SqlSubQueryResult
case class SongsSatisfyingQuery(songs: List[Song]) extends SqlSubQueryResult

case class DirectoryToAnalyze(directory: File)         extends SqlSubQueryResult
case class FileTypesToAnalye(fileTypes: Set[FileType]) extends SqlSubQueryResult
