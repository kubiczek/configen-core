package pl.kubiczek.configen.plugins

import pl.kubiczek.configen.PropertyGenerator

class Echo extends PropertyGenerator {
  def generate(args: Array[String]) = args.foldLeft("")(_ + " " + _).trim
}