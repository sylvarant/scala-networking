package networkframework.core.algorithm
import scala.collection.mutable.HashMap
import scala.collection.mutable.Queue

import networkframework.core.graph.Edge
import networkframework.core.graph.Node

object Algorithms {

  /**
   * Gets the actual distance between 2 nodes
   * edgeFun holds an optional function that can be used to calculate the distance of a node.
   * Returns -1.0 if there is no path
   */
  def minDist(s: Node, t: Node, edgeFun: (Edge => Double) = (Edge => 1.0)):Double = {
   if (s == t) { return 0.0 ;}
   var queue : Queue[Edge] = new Queue
   var map : HashMap[Node, Double] = new HashMap
   map.put(s,0.0)
   
   for (e <- s.getEdges) yield (queue.enqueue(e))
   
   var min = Double.MaxValue
   /** note this could run long on big graphs, not the best implementation */
   while(!queue.isEmpty) {
     var e : Edge = queue.dequeue
     map.get(e.left) match {
       case Some(a) => map.get(e.right) match {
         case Some(b) => if (a + edgeFun(e) < b) {map.update(e.right, a + edgeFun(e)); for (edge <- e.right.getEdges) yield (queue.enqueue(edge))}
         case None => map.put(e.right, a+edgeFun(e)); for (edge <- e.right.getEdges) yield (queue.enqueue(edge))
       }
       case None => map.get(e.right) match {		// this implies a bidirectional link?
         case Some(b) => map.put(e.left, b + edgeFun(e)); for (edge <- e.left.getEdges) yield (queue.enqueue(edge))
         case None => throw new Exception("Map Initialization Error")
       }
     }
     
   }
   
   map.get(t) match {
     case Some(x) => return x
     case None => return -1.0
   }
  }
  
  /**
   * Constructs a path from S to T.
   * assert s != t
   */
   def minPath(s: Node, t: Node, edgeFun: (Edge => Double) = (Edge => 1.0)):Seq[Node] = {
	  if (s == t) { throw new Exception("Source and Destination nodes are identical")}
	  var queue : Queue[Edge] = new Queue
	  var map : HashMap[Node, Double] = new HashMap
	  var prev : HashMap[Node, Node] = new HashMap
	  map.put(s,0.0)

	  for (e <- s.getEdges) yield (queue.enqueue(e))

	  var min = Double.MaxValue
	  /** note this could run long on big graphs, not the best implementation */
	  while(!queue.isEmpty) {
		  var e : Edge = queue.dequeue
		  map.get(e.left) match {
		  case Some(a) => map.get(e.right) match {
		  	case Some(b) => if (a + edgeFun(e) < b) {map.update(e.right, a + edgeFun(e))
			  prev.update(e.right, e.left)
			  for (edge <- e.right.getEdges) yield (queue.enqueue(edge))}
		  	case None => map.put(e.right, a+edgeFun(e)); 
		  	  prev.put(e.right, e.left)
		  	  for (edge <- e.right.getEdges) yield (queue.enqueue(edge))
		  	  }
		  case None => map.get(e.right) match {		// this implies a bidirectional link?
		  	case Some(b) => map.put(e.left, b + edgeFun(e)); 
		  	  prev.update(e.left, e.right)
		  	  for (edge <- e.left.getEdges) yield (queue.enqueue(edge))
		  	case None => throw new Exception("Map Initialization Error")
		  }
	  	  }
	  }

	  	  
	  def constructPath() = {
	    var seq : List[Node] = Nil
	    var p = t
	    while (p != s) {
	      seq ::= p
	      p = prev.get(p) match {
	        case Some(node) => node
	        case _ => throw new Exception("Path construction error")
	      }
	    }
	    seq ::= p
	    seq
	  }
	  
	  map.get(t) match {
	  	case Some(x) => constructPath()
	  	case None => return Nil
	  }
  }
}