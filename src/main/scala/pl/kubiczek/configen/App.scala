package pl.kubiczek.configen
import java.io.File

/**
 * @author kubiczek
 */
object App {
  def main(args : Array[String]) {
    Parser(args(0)).parse().save(args(1))
  }
}
