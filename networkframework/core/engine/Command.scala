package networkframework.core.engine

import networkframework.core.graph._
import networkframework.core.io.Log

/**
 * the target to be used
 */
private[networkframework] object CommandTarget {
  var engine : Option[SimulationEngine] = None
}

/**
 * The abstract Command class
 */
abstract class Command() {
	
	/**
	 * Class member defining the round to be executed only 
	 * usefull for eventbased
	 */
    var executeAt : Option[Int] = None
  
	/**
	 * execute the command
	 */
	def execute
	
	/**
	 * send the command to the engine
	 */
	def send = CommandTarget.engine match{
	  case None => throw new Exception("error ! no engine has been set !")
	  case Some(x) => x.exec(this)
	}
	
	def step : Int = executeAt match {
	  case None => CommandTarget.engine match  { 
	    case None => -1
	    case Some(x) => x.step
	  }
	  case Some(x) => x
	}
}


/**
 * Skip command = do nothing
 */
class Skip() extends Command {
  def execute = {Log.log("[COMMAND] Doing nothing")}
}

/**
 * Example command connect
 */
class Connect(val g: Graph, val e : Edge) extends Command {
  def execute = g.add(e);
}

/**
 * Example command Die
 */
sealed class Die(val n : Node) extends Command {
	def execute = Log.log("[COMMAND] node "+n.getId+" died"); n.remove ; n.graph.remove(n); 
}

/**
 * Example add command
 */
class Add(val n : Node,val g: Graph) extends Command {
	def execute = g.add(n)
}

/**
 * An Event Command. Will be executed at a certain step 't'.
 */
abstract class Event(t : Integer) extends Command {
  executeAt = Some(t)
}