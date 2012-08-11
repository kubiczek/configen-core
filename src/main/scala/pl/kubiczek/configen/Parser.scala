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
      case Configuration(data) =>
        new Configuration(data.map(x => if (x._2.startsWith("$")) (x._1, eval(x._2.substring(1))) else x))
    }
  }

  def eval(expr: String) = {
    val splited = expr.split(" ")
    val propGen = Class.forName(splited.head).newInstance.asInstanceOf[PropertyGenerator]
    propGen.generate(splited.tail)
  }

}