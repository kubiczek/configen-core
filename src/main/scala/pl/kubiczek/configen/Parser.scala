package pl.kubiczek.configen

import org.streum.configrity._
import java.io.File

object Parser {
  def apply(cfgFile: File) = {
    new Parser(Configuration.load(cfgFile.getAbsolutePath(), FlatFormat))
  }
}

class Parser(config: Configuration) {

  def parse() = {
    config match {
      case Configuration(data) => new Configuration(data.map {
        case x @ (key, value) => if (value.startsWith("$")) (key, eval(value.substring(1))) else x
      })
    }
  }

  private def eval(expr: String) = {
    val splited = substitute(expr.split("[\\$\\{,\\}]").toList).reduce(_ + _).split(" ")

    Class.forName(splited.head)
      .newInstance
      .asInstanceOf[PropertyGenerator]
      .generate(splited.tail)
  }

  private def substitute(ls: List[String]): List[String] = ls match {
    case x :: y :: ys => if (x == "") config[String](y) :: substitute(ys) else x :: substitute(y :: ys)
    case ys @ x :: Nil => ys
    case Nil => Nil
  }

}