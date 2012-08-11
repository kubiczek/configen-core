package pl.kubiczek.configen

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import pl.kubiczek.configen.plugins.CurrentDateTime
import pl.kubiczek.configen.plugins.Echo

@RunWith(classOf[JUnitRunner])
class ParserTest extends FunSuite {

  def autoFile[A](content: String = "")(body: File => A) = {
    val f = File.createTempFile("configen-temaplate", ".conf")
    try {
      if (content != "") {
        val out = new PrintWriter(f)
        out println content
        out close
      }
      body(f)
    } finally {
      f delete
    }
  }

  test("parse configuration file without configen's expression") {
    val s =
      """
      foo = true
      bar = 2
      baz = "hello world"
      """
    autoFile(s) { file =>
      val config = Parser(file).parse()
      assert(config[Boolean]("foo") === true)
      assert(config[Int]("bar") === 2)
      assert(config[String]("baz") === "hello world")
    }
  }

  test("parse configuration file with CurrentDateTime configen's expression") {
    val s =
      """
      foo = true
      bar = 2
      baz="$pl.kubiczek.configen.plugins.CurrentDateTime ddMMyyyy"
      """
    autoFile(s) { file =>
      val config = Parser(file).parse()
      assert(config[Boolean]("foo") === true)
      assert(config[Int]("bar") === 2)
      assert(config[String]("baz") === new CurrentDateTime().generate(Array("ddMMyyyy")))
    }
  }

  test("parse configuration with property evaluation") {
    val s =
      """
      fmt = yyyyMMdd
      baz = "$pl.kubiczek.configen.plugins.CurrentDateTime ${fmt}"
      bar = "$pl.kubiczek.configen.plugins.Echo format ${fmt}"
      """
    autoFile(s) { file =>
      val config = Parser(file).parse()
      assert(config[String]("fmt") === "yyyyMMdd")
      assert(config[String]("baz") === new CurrentDateTime().generate(Array("yyyyMMdd")))
      assert(config[String]("bar") === "format yyyyMMdd")
    }
  }
}