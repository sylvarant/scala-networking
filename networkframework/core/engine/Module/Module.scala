package networkframework.core.engine.Module

import networkframework.core.engine.SimulationEngine
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.attribute.Attributes
import networkframework.core.graph._
import scala.collection.mutable.HashMap
import networkframework.core.monitoring._
import networkframework.core.statistic.Statistics
import scala.collection.mutable.Set
import networkframework.core.statistic.Statistic

/**
 * Module trait (interface)
 */
trait Module {
  /**
   * Name of the module
   */
  var key : String = ""
	
	/**
	 * Dependencies (other required models)
	 */
	var requirements : Set[String] = Set[String]();
	
	/**
	 * Event listeners for this module
	 */
	var handlers : HashMap[String, Set[(networkframework.core.monitoring.Event) => Unit]] = new HashMap[String, Set[(networkframework.core.monitoring.Event) => Unit]]();
	
	/**
	 * Node/Edge attributes for this module
	 */
	var attributes : Set[Attribute[_ <: GraphPart,_]] = Set[Attribute[_ <: GraphPart,_]]();
	
	/**
	 * Statistics for this module
	 */
	var statistics : Set[Statistic[_ <: Any]] = Set[Statistic[_ <: Any]]();
	
	/**
	 * Add handler
	 */
	protected def addHandler(key: String, e: (Event) => Unit)
	{
	  if(!handlers.contains(key))
	    handlers+= key -> Set[(Event) => Unit]();
	    
	  handlers.get(key).map(l => l+=e)
	}
	
	/**
	 * Module installation: adds statistics, binds event listeners and adds attributes
	 * notifies the monitor of the installation
	 */
	def install(e : SimulationEngine){
		statistics.foreach(s => Statistics.add(s))
		enable()
    attributes.foreach(Attributes.addAttribute(_));
    e.modules += key -> this
    Monitor.dispatch(new ModuleInstalledEvent(this));
    Monitor.dispatch(new ModuleXInstalledEvent(this));
   
  }
	
	def enable(){
		statistics.foreach(s => s.enable())
    handlers.foreach{case (key,handlers) => handlers.foreach(handler => Monitor.bind(key,handler))}
	}
	
	def disable(){
	  statistics.foreach(s => s.disable())
	  handlers.foreach{case (key,handlers) => handlers.foreach(handler => Monitor.unbind(key,handler))}
	}
}


