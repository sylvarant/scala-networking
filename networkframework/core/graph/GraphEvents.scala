package networkframework.core.graph
import networkframework.core.monitoring.Event

case class NewNodeEvent(node : Node) extends Event
{
  key = "graph.newNode"
}

case class RemoveNodeEvent(node : Node) extends Event
{
  key = "graph.removeNode"
}

case class NewEdgeEvent(edge : Edge) extends Event
{
  key = "graph.newEdge"
}

case class RemoveEdgeEvent(edge: Edge) extends Event
{
  key = "graph.removeEdge"
}
