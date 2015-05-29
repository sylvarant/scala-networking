package networkframework.core.graph.attribute
import networkframework.core.graph.GraphPart
import networkframework.core.monitoring.Event


case class AttributeUpdateEvent(u: GraphPart, attribute:String, value: Any) extends Event
{
	key = "attribute.Update"
}
  
case class AttributeXUpdateEvent(u: GraphPart, attribute:String, value: Any) extends Event
{
	key = "attribute."+ attribute(0).toUpperCase + attribute.substring(1, attribute.length) +"Update"
}

case class AttributeXYUpdateEvent(u: GraphPart, attribute:String, value: Any) extends Event
{
	key = "attribute.part"+ u.getId + attribute(0).toUpperCase + attribute.substring(1, attribute.length) +"Update"
}