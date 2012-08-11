package pl.kubiczek.configen

trait PropertyGenerator {
  def generate(args: Array[String]): String
}