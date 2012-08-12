package pl.kubiczek.configen.plugins

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import java.util.Date

@RunWith(classOf[JUnitRunner])
class CurrentDateTimeTest extends FunSuite {

  test("choke when calling generate with no args (empty array)") {
    intercept[ArrayIndexOutOfBoundsException] {
      new CurrentDateTime().generate(Array())
    }
  }

  test("get current date") {
    val result = new CurrentDateTime().generate(Array("ddMMyyy"))
    assert(result === new SimpleDateFormat("ddMMyyy").format(new Date))
  }

}