package networkframework.core.graph.messaging

import scala.collection.mutable.Queue
import scala.Some

/**
 * A basic abstract implementation of a messaging edge
 * This edge has a FIFO queue and returns 1 message at a time
 */
abstract class BasicMessagingEdge(a: MessagingNode, b:MessagingNode) extends MessagingEdge {
  
  override def left = a
  
  override def right = b
  
  /** can a package be popped? */
  var next = false
  
  /** a message queue */
  var queue = new Queue[Packet]
  
  /** Add a package to the queue */ 
  def add(p: Packet) = queue.enqueue(p)
  
  /** Remove packages that are processed */
  def poll = {if (hasNext) {next = !next ; Some(queue.dequeue())} else  None }
  
  /** Does this edge have a new package available? */
  def hasNext = next & !queue.isEmpty
  
  /** Advance through time */
  def advance = next = true
   
}

/**
 * An edge on whic messages can pass in one direction
 */
class UnidirectionalMessagingEdge(a: MessagingNode, b:MessagingNode) extends BasicMessagingEdge(a,b) {
  edgeType = "unidirectional"  
}

/**
 * An edge on which messages can pass in two directions
 */
class BidirectionalMessagingEdge(a: MessagingNode, b:MessagingNode) extends BasicMessagingEdge(a,b) {
  edgeType = "bidirectional"
  
}