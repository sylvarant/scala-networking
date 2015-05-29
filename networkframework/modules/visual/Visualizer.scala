package networkframework.modules.visual
import networkframework.core.graph.Graph
import networkframework.core.graph.Node
import networkframework.core.graph.Edge
import networkframework.core.graph.NewNodeEvent
import networkframework.core.graph.NewEdgeEvent
import networkframework.core.graph.RemoveEdgeEvent
import networkframework.core.graph.RemoveNodeEvent
import networkframework.core.monitoring.Event
import networkframework.core.engine.SimulationEngine
import networkframework.core.engine.SimulationStartEvent

/**
 * Visualizer module base
 */
trait Visualizer {
  
  /**
   * Render network on new simulation
   */
  def visualizeHandler(e : Event){
    e match{
      case SimulationStartEvent(engine) => visualize(engine.graph)
      case _ =>
    }
  }
  
  /**
   * Render network
   */
  def visualize(g: Graph){
    clear();
    g.foreach(addNode);
    g.foreachEdge(addEdge);
  }
  
  /**
   * Render new node
   */
  def addNodeHandler(e : Event){
    e match{
      case NewNodeEvent(n) => addNode(n)
      case _ =>
    }
  }
  
  /**
   * Render node
   */
  def addNode(n : Node);
  
  /**
   * Render new edge
   */
  def addEdgeHandler(e : Event){
    e match{
      case NewEdgeEvent(e) => addEdge(e)
      case _ =>
    }
  }
  
  /**
   * Render edge
   */
  def addEdge(e : Edge);
  
  /**
   * Remove node
   */
  def removeNodeHandler(e : Event){
    e match{
      case RemoveNodeEvent(n) => removeNode(n)
      case _ =>
    }
  }
  def removeNode(n : Node);
  
  /**
   * Remove edge
   */
  def removeEdgeHandler(e : Event){
    e match{
      case RemoveEdgeEvent(e) => removeEdge(e)
      case _ =>
    }
  }
  def removeEdge(e: Edge);
  
  /**
   * Clear the simulation
   */
  def clear();

}