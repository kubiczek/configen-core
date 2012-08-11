package pl.kubiczek.configen.plugins

import pl.kubiczek.configen.PropertyGenerator
import java.util.Date
import java.text.SimpleDateFormat

class CurrentDateTime extends PropertyGenerator {
  
  def generate(args: Array[String]) = new SimpleDateFormat(args(0)).format(new Date)

}