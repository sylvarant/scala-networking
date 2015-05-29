package networkframework.core.graph
import networkframework.core.engine._

/**
 * Abstract class Node
 */
abstract class BasicNode extends Node{

  def this(graph : Graph) = {this(); _graph = Some(graph)}
}

