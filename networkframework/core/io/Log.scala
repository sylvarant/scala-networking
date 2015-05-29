package networkframework.core.io
import networkframework.core.engine.SimulationEngine

object Log {
	
  def setEngine(e : SimulationEngine) = {engine = Some(e)}
  
  var engine : Option[SimulationEngine] = None
  
  def log(s : String) = {
    var str : String = ""
	  engine match {
	    case Some(e) => str = e.stamp + s;
	    case None => str = s
	  }
    println(str)
    
  }
  
  var eventLogger = true;
  def enableEvents = {eventLogger = true }
  def disableEvents = {eventLogger = false}
  
  def debug(s : String) = {println("[DEBUG] " + s)}
  
  def warning(s : String) = {println("[WARNING] "  + s)}
  
  def event(s : String) = {if (eventLogger) println("[EVENT] "  + s +" fired")}
}