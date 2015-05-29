package networkframework.core.graph.messaging
import networkframework.core.graph.Node
import networkframework.core.graph.Edge
import scala.util.Random

/**
 * A trait that specifies a router
 */
trait Router {

  /** returns the next destination on a certain routing path */
  def route[T <: MessagingEdge](a: MessagingNode, b:MessagingNode) : T
}

/**
 * A router that randomly picks an edge over which to transmit the message
 */
class RandomRouter extends Router {
  
  /** Every time a random edge of a is chosen to route the message over */
  override def route[MessagingEdge](a: MessagingNode, b: MessagingNode) = {
    val random = new Random
    val border = a.getEdges.size
    val r = random.nextFloat

    val edge = a.getEdges(Integer.parseInt(Math.round(Math.floor(r*a.getEdges.size)).toString()))	// THIS HAS TO BE POSSIBLE IN AN EASIER FASHION. O MY GOD
    
    edge match {
      case e : MessagingEdge => e
      case _ => throw new Exception("Unsupported Edge Type")
    }
  }
  
  
}