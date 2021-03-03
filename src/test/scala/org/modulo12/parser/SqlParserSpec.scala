package org.modulo12.parser

import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.antlr.v4.runtime.ANTLRInputStream
import org.modulo12.sql.SqlParser

class SqlParserSpec extends AnyFlatSpec with should.Matchers with Inside {
  "parser" should "evaluate to not implemented" in {
    assertThrows[scala.NotImplementedError] {
      SqlParser.parse("SELECT * FROM some_dir")
    }
  }
}
