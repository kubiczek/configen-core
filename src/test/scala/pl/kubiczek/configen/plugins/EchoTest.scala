package pl.kubiczek.configen.plugins

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class EchoTest extends FunSuite {

  test("echo empty array") {
    val result = new Echo().generate(Array())
    assert(result === "")
  }

  test("echo not empty array") {
    val result = new Echo().generate(Array("hello", "world!"))
    assert(result === "hello world!")
  }
}