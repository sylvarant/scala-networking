package networkframework.core.graph.messaging
import scala.collection.mutable.Queue
import scala.collection.mutable.Stack

/**
 * This messaging node takes 1 full turn to route a message
 */
abstract class BasicMessagingNode extends MessagingNode{
  
  /** 
   * The act method processes the messages
   * Don't overwrite this, expand it :).
   */
  def act = {
    poll match {
      case Some(p) => p.current = this ; if (p.destination != this) p.send
      case None => Unit
    }
  }
  
  def receive(p: Packet) = if (p.destination == this) process(p)
  
}

/**
 * A node that has a LIFO policy when handling messages
 */
class LIFOMessagingNode extends BasicMessagingNode {
  
  var messageQueue = new Queue[Packet]
  
  override def receive(p : Packet) = {super.receive(p) ; p.current = this; messageQueue.enqueue(p)}
  
  def poll = if (!messageQueue.isEmpty) Some(messageQueue.dequeue) else None
  
}

/**
 * A node that has a FIFO policy when handling messages
 */
class FIFOMessagingNode extends BasicMessagingNode {
  
  var messageQueue = new Stack[Packet]
  
  override def receive(p : Packet) = {super.receive(p); messageQueue.push(p)}
  
  def poll = if (!messageQueue.isEmpty) Some(messageQueue.pop()) else None
}