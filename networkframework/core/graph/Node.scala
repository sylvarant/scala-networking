package networkframework.core.graph
import networkframework.core.graph.messaging._
import networkframework.core.graph.UnidirectionalEdge
import networkframework.core.graph.BidirectionalEdge

trait Node extends GraphPart{

  /**
   * class member edges
   */
  var edges : List[Edge] = Nil
  
  /**
   * bidirectional relation to graph
   */
  private[networkframework] var _graph : Option[Graph] = None
  
  /**
   * Add a edge to the Node
   */
  def addEdge(e: Edge) {
    graph.add(e);
  }
  
  /**
   * Get the graph the Node is connected to
   */
  def graph : Graph = _graph match {
    case None => throw new Exception("No graph is set")
    case Some(x) => x
  }
  
  /**
   * Fetch neighbours 
   * make difference between types of edges ?
   */
  def neighbours : List[Node] = for(e <- edges) yield (if(e.right == this) e.left else e.right) 
  
  /**
   * See whether a given node is a neighbour of this node
   */
  def isNeighbour(other : Node) : Boolean = neighbours.contains(other)

  /**
   * Fetch all edges
   */
  def getEdges : List[Edge] = edges
  
  /**
   * Remove all edges from the neighbours
   */
  def remove = edges.foreach(e => (if(e.right == this) e.right.removeEdge(e) else e.left.removeEdge(e))) 
  
  /**
   * get rid of a certain edge
   */
  def removeEdge(e : Edge) = {edges = edges.filterNot(e == )}
   
  /**
   * the real deal
   */
  def act
  

  def accept(visitor : Visitor) = visitor.visit(this)
}