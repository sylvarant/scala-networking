package networkframework.core.graph.messaging
import networkframework.core.graph.Edge
import scala.collection.mutable.Queue
import scala.Some

/**
 * The specification of a messaging edge, i.e. an edge on which messages can be transmitted
 */
trait MessagingEdge extends Edge {

  override def left : MessagingNode
  
  override def right : MessagingNode
  
  /** Add a package to this edge */
  def add(p: Packet)

  /** Process the packages on this edge */
  final override def process = {
    advance
    while (hasNext) {
      poll match {
        case Some(p) => (p.currentDestination).receive(p)
        case None => Unit
      }
    }
  }
  
  /** Remove one package at a time from this edge */
  def poll: Option[Packet]
  
  /** Is there an extra package to remove? */
  def hasNext : Boolean
  
  /** Advance the state of the edge through time */
  def advance : Unit
}


