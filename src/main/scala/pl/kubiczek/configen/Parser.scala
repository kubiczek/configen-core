package pl.kubiczek.configen

import org.streum.configrity._
import java.io.File
import scala.collection.mutable.Map

object Parser {
  def apply(cfgFile: File): Parser = {
    Parser(cfgFile.getAbsolutePath())
  }

  def apply(cfgFilename: String) = {
    new Parser(Configuration.load(cfgFilename, FlatFormat))
  }
}

class Parser(config: Configuration) {

  private val cache = Map[String, EvalState]()

  private abstract sealed class EvalState
  private case object InProgress extends EvalState
  private case class Evaluated(value: String) extends EvalState

  def parse() = {
    config match {
      case Configuration(data) => new Configuration(
        for ((key, value) <- data) yield (key, eval(key, value)))
    }
  }

  private def eval(key: String, expr: String) = {
    if (expr.startsWith("$")) {

      if (cache.contains(key)) {
        cache(key) match {
          case InProgress => throw new IllegalStateException("cyclic dependency detected for property " + key)
          case Evaluated(value) => value
        }
      } else {
        cache += (key -> InProgress)
        val splited = substitute(expr.substring(1).split("[\\$\\{,\\}]").toList).reduce(_ + _).split(" ")
        val result = Class.forName(splited.head)
          .newInstance
          .asInstanceOf[PropertyGenerator]
          .generate(splited.tail)

        cache += (key -> Evaluated(result))
        result
      }

    } else {
      expr
    }
  }

  private def substitute(ls: List[String]): List[String] = ls match {
    case x :: y :: ys => if (x == "") eval(y, config[String](y)) :: substitute(ys) else x :: substitute(y :: ys)
    case ys @ x :: Nil => ys
    case Nil => Nil
  }

}