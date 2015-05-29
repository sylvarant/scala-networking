package applications
import networkframework.core.graph.Node
import networkframework.core.engine.SimulationEngine

/**
 * An Application trait
 */
trait Application {
  
  /** The name of this application */
  var name = "Unknown Application"
    
  /** A description of the application */
  var description = ""
    
  /** The engine of this application */
  var _engine : Option[SimulationEngine] = None

 
  /** Get the engine */
  def engine : SimulationEngine = _engine match {
    case Some(x) => x
    case None => throw new Exception("No engine has been set");
  }
  
  /** Starting the application */
  def start : Unit
    
  /** Initialize everything */
  def init : Unit
}