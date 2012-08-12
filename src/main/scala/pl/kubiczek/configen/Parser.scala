package pl.kubiczek.configen

import org.streum.configrity._
import java.io.File

object Parser {
  def apply(cfgFile: File): Parser = {
    Parser(cfgFile.getAbsolutePath())
  }

  def apply(cfgFilename: String) = {
    new Parser(Configuration.load(cfgFilename, FlatFormat))
  }
}

class Parser(config: Configuration) {

  def parse() = {
    config match {
      //      case Configuration(data) => new Configuration(data.map {
      //        case (key, value) => (key, eval(value))
      //      })
      case Configuration(data) => new Configuration(
        for ((key, value) <- data) yield (key, eval(value)))
    }
  }

  private def eval(expr: String) = {
    if (expr.startsWith("$")) {
      val splited = substitute(expr.substring(1).split("[\\$\\{,\\}]").toList).reduce(_ + _).split(" ")

      Class.forName(splited.head)
        .newInstance
        .asInstanceOf[PropertyGenerator]
        .generate(splited.tail)
    } else {
      expr
    }
  }

  private def substitute(ls: List[String]): List[String] = ls match {
    case x :: y :: ys => if (x == "") eval(config[String](y)) :: substitute(ys) else x :: substitute(y :: ys)
    case ys @ x :: Nil => ys
    case Nil => Nil
  }

}