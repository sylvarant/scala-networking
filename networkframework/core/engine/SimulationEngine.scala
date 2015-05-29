package networkframework.core.engine
import networkframework.core.graph._
import networkframework.core.monitoring._
import networkframework.core.graph.messaging.RouterTarget
import networkframework.core.graph.messaging.Router
import networkframework.core.graph.messaging.MessagingEdge
import networkframework.core.io.Log
import scala.collection.mutable.HashMap
import networkframework.core.engine.Module.Module
import networkframework.core.engine.Module.ModuleNotFoundException
import networkframework.core.statistic.Statistics
/**
 * class SimulationEngine
 * The entity running the simulation
 */
trait SimulationEngine{
  
  /**
   * Wait sleep millisecons between steps
   */
  var sleep : Int = 0;
  
  /**
   * Simulated network
   */
  var graph : Graph = null;
  
  /**
   * Installed modules
   */
  var modules = HashMap[String, Module]();
  
  /**
   * Install a module
   */
  def installModule(m : Module){
    
    /** Check if already installed */
    if(!modules.contains(m.key)){
    
      /** Check dependencies */
      m.requirements.foreach(r => if(!modules.contains(r)) throw new ModuleNotFoundException(r))
      
      /** Install module */
      m.install(this);
    }
  }
    
  
  /**
   * Set the command target
   */
   CommandTarget.engine = Some(this) ;
   Log.setEngine(this);
   Statistics.engine = this;
  
  /**
   * Set a (custom) router
   */
  def setRouter(r : Router) = RouterTarget.setRouter(r)
  
  /**
   * class members 
   */
  protected var stack : List[Command] = Nil
  
  /**
   * Which step (virtual time) we are
   */
  var step : Int = 0
  
  
  /**
   * the simulation to be done
   */
  private[engine] def simulate
  
  /**
   * Do some configuration, notify the monitor and run the simulation
   */
  def start{
    if(graph == null) throw new Exception("Graph has not been set in simulationEngine")
    running = true
    Monitor.dispatch(new SimulationStartEvent(this));
    while(!stop && running){
      simulate;
    }
    running = false;
    Log.log("simulation halted after " + step + " steps"); 
    Monitor.dispatch(new SimulationDoneEvent(this));
  }
  
  /**
   * Whether we are running or not
   */
  var running : Boolean = false;
  
  protected def updateStep(){
    step +=1; 
    Monitor.dispatch(SimulationUpdateEvent(this)); 
    if(sleep > 0) Thread.sleep(sleep);
  }
  
  /** the list of Terminating Conditions of this engine */
  var terminators : List[Terminator] = Nil
  
  /** see if any of the terminators is fulfilled */
  def stop : Boolean = terminators.filter(t => (t.stop)).size > 0
  
  /** Add a terminating condition */
  def addTerminator(t : Terminator) = terminators ::= t
  
  /** Remove a terminating condition */
  def removeTerminator(ter : Terminator) = terminators = terminators.filter(t => t != ter) 
  
  /**
   * specify how to execute a command -> this function is called by the actors
   */
  def exec(c : Command)
  
  /**
   * Provide a timestamp from the current simulation state
   */
  def stamp = "[@"+step+"] "
}



/**
 * class TurnBasedEngine
 * Have each actor perform 1 action and loop
 */
class TurnBasedEngine extends SimulationEngine {
  
  /**
   * specify the simulation
   */
  override def simulate : Unit = {
  graph.edges.foreach(p => p.process)
	graph.foreach({n => n.act ; updateStep()})
  }
  
  /**
   * specify how to execute a command -> this function is called by the actors
   */
  override def exec(c : Command) = c.execute  
}




/**
 * class RoundBasedEngine
 * Have each actor perform an action but delay till everyone is done
 */
class RoundBasedEngine extends SimulationEngine {

  /**
   * Empty the stack after his, otherwise the commands will be repeated
   */
  private def doround = { 
    graph.edges.foreach(p => p.process)
    val runstack : List[Command] = stack
    stack = Nil
    runstack.foreach(c => c.execute)
    updateStep()
  }
  
  /**
   * specify the simulation
   */
  override def simulate = {
    	graph.foreach(n => n.act)
    	doround
  }
  
   /**
   * specify how to execute a command -> this function is called by the actors
   */
  override def exec(c : Command) = stack ::= c 
  
  override def stamp = "[@round: "+step+"]"
}


/**
 * class EventBasedEngine
 * Have each actor perform an action when the time he wants has come
 */
class EventBasedEngine extends SimulationEngine {

  /**
   * execute the necessary events. (this is disturbingly inefficient --> PriorityQueue)
   * Also removes events from the past
   */
  private def doevents =  {
	var remove : List[Command] = Nil;
    stack.foreach(c => if(c.step == step){c.execute; remove ::= c})
    stack = stack.filter(c => !remove.contains(c))
  }
  
  /**
   * specify the simulation
   */
  override def simulate = {
		graph.edges.foreach(p => p.process)
    	/** fetch new events from actors */
    	graph.foreach({n => n.act})
    	doevents
    	updateStep()
  }
  
   /**
   * specify how to execute a command -> this function is called by the actors
   */
  override def exec(c : Command) = stack ::= c 
}