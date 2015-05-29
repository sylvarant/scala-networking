package networkframework.core.monitoring
import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import networkframework.core.io.Log

/**
 * Monitor is the communication system of the framework.
 * It handles all incoming events and dispatches them to every interested function
 */
object Monitor {
  
  /**
   * Maps events to interested functions
   */
	var subscribers = HashMap[String, Set[eventHandler]]()
	
	type eventHandler = (Event) => Unit
	
	/**
	 * Dispatch event to interested functions
	 */
	def dispatch(e : Event){
	  Log.event(e.key)
		subscribers.get(e.key).map(l => l.map(f => f(e)))
	}
	
	/**
	 * Binds functions to events
	 */
	def bind(key: String, e: eventHandler)
	{
	  if(!subscribers.contains(key))
	    subscribers += key -> Set[eventHandler]();
	  
	  subscribers.get(key).map(l => l+=e)
	}
	
	/**
	 * Unbinds functions to events
	 */
	def unbind(key: String, e: eventHandler){
	  subscribers.get(key) match {
	    case Some(x) => x.remove(e);
	  }
	}
	
}