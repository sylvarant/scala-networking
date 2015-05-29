package networkframework.core.graph
import scala.util.Random
import scala.collection.mutable.Set
import scala.collection.mutable.HashSet
import scala.Some
import networkframework.core.graph.messaging.MessagingEdge
import networkframework.core.monitoring.Monitor
import networkframework.core.graph.messaging.UnidirectionalMessagingEdge
import networkframework.core.graph.messaging.BidirectionalMessagingEdge


trait Graph {
 /**
   * private member node list
   */
  private var nodes : List[Node] = Nil
  
  def getNodes = nodes
  
  /**
   * Random generator for ID's for the nodes
   */
  val random = Random
  
  /**
   * assign id's to the nodes
   */
 // private def assignIds = {var i = 0; for (n <- nodes) yield {n.setId(i); i+=1}}
  

  
  /**
   * add a member to the graph
   */
  def add(n : Node) = { n._graph = Some(this) ; nodes ::= n ; Monitor.dispatch(new NewNodeEvent(n))}
 
  /**
   * remove a node from the graph
   */
  def remove(n : Node) = { n.remove ; nodes = nodes.filterNot(n ==); Monitor.dispatch(new RemoveNodeEvent(n))}
  

  /**
   * get nodes with a certain property
   */
  def filter[T<: Node](func : (Node => Boolean)) : List[T]  = {
    val list = nodes.filter(func(_))
    var r : List[T] = Nil
    for(n <- list) yield (n match { case t:T => r ::= t; case _ => Unit } )
    r
  }
  
  /**
   * run over all nodes
   */
  def foreach[B](func: (Node => B)) : Unit = nodes.foreach(func(_))
  
  /**
   * run over all edges
   */
  def foreachEdge[B](func: (Edge => B)) : Unit = edges.foreach(func(_))
  
  /**
   * return member of graph based on index
   */
  def  get(index : Int) : Node = {
     if (index >= 0 && index < size) nodes(index)
     else throw new NoSuchElementException
  }
  
  
  
  /**
   * Get all the edges of this graph
   */
  def edges : Set[Edge] = {
    var l : Set[Edge] = new HashSet[Edge]
    for (n <- nodes) yield (
        for (e <- n.getEdges) yield (l.add(e))
      )
    l
  }
  
  /**
   * Remove the edge from the graph
   */
  def removeEdge(e : Edge) = {e.remove;e.left.removeEdge(e);e.right.removeEdge(e);Monitor.dispatch(new RemoveEdgeEvent(e));}
  
  /**
   * add an edge
   */
  def add(e : Edge) { 
    e match {
      case UnidirectionalEdge(l,r) => l.edges ::= e;
      case BidirectionalEdge(l,r) => l.edges ::= e; r.edges ::= e;
      case u : UnidirectionalMessagingEdge => u.left.edges ::= e
      case b : BidirectionalMessagingEdge => b.left.edges ::= e; b.right.edges ::= e;
      case _ => throw new Exception("Unsupported type of Edge, please override the addEdge method")
    }
     Monitor.dispatch(new NewEdgeEvent(e));
  }
 

  
  
  /**
   * get size
   */
  def size = nodes.size
  
  def accept(visitor : Visitor) = visitor.visit(this)
  

  
}