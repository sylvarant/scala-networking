package networkframework.core.graph.attribute
import scala.collection.mutable.Set
import networkframework.core.graph.GraphPart
import scala.collection.mutable.HashMap

/**
 * Keeps all the attributes
 */
object Attributes {
	var attributes = HashMap[String,Attribute[_ <: GraphPart,_ <: Any]]();
	
	def addAttribute(attr : Attribute[_ <: GraphPart, _ <: Any]){
	  attributes += attr.key -> attr
	}
	
	def setAttribute[T](key : String, part: GraphPart, value: T) = {
	  if (attributes.contains(key)) attributes.apply(key).asInstanceOf[Attribute[GraphPart,T]].set(part, value)
	  else {throw new NoSuchAttributeException(key)} 
	}
	
	/** First check if this key actually exists */
	def getAttribute[T](key : String) : Attribute[GraphPart, T] = {
	  attributes.apply(key).asInstanceOf[Attribute[GraphPart,T]]
	}
	
}