package networkframework.core.graph.messaging
import networkframework.core.engine.Command
import networkframework.core.graph.Edge
import networkframework.core.io.Log

private[networkframework] object RouterTarget {
  private var r : Option[Router] = None
  
  def router = r match {
    case Some(x) => x
    case None => Log.warning("No router set, random router generated"); new RandomRouter
  }
  
  private[networkframework] def setRouter(nr : Router) = r = Some(nr) 
}

/**
 * A simple Packet that can be sent from one node to another.
 */
class Packet(s: MessagingNode, t: MessagingNode) extends Command {

  
  /** define the router target for this package. This target is set when creating the engine */
  def router = RouterTarget.router
  
  /** the sender of the packet */
  val sender = s
  s.receive(this)
  /** the current holder of the packet */
  var current = s
  
  /** The intermediary recipient of the packet */
  var currentDestination = t
  
  /** the recipient for this packet */
  val destination = t
  
  /** Executing a packet means forwarding it to the next recipient */
  final override def execute = {
    /** Get the edge over which to route */
    val edge: MessagingEdge = router.route(current,destination)
    forward(edge)
  }
  
  /** forward this packet over an edge */
  def forward(e: MessagingEdge) = {
    if (e.left == current) currentDestination = e.right
    else if (e.right == current) currentDestination = e.left
    
    e.add(this)
  }
  
}

/**
 * A simple message that can hold a string
 */
class Message(val msg : String, s : MessagingNode, t : MessagingNode) extends Packet(s,t) {
  
}