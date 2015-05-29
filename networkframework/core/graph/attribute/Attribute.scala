package networkframework.core.graph.attribute
import networkframework.core.graph.GraphPart
import networkframework.core.monitoring.Monitor
import scala.collection.mutable.HashMap



/**
 * Attribute for node/edge
 */
class Attribute[U <: GraphPart, T <: Any](val key: String, var default: T) {

  /**
   * Node/Edge -> Value mapping
   */
  var owners = new HashMap[U, T]
  
  /**
   * Get value for Node/Edge u
   */
  def get(u: U) : T = {
      owners.getOrElse(u,default);
  }
   
  /**
   * Set value for Node/Edge u
   */
  def set(u: U, t: T){
    owners += u.asInstanceOf[U] -> t 
    Monitor.dispatch(new AttributeUpdateEvent(u,key,t));
    Monitor.dispatch(new AttributeXUpdateEvent(u,key,t));
    Monitor.dispatch(new AttributeXYUpdateEvent(u,key,t));
  }
  
}