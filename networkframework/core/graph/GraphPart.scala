package networkframework.core.graph
import networkframework.core.graph.attribute.Attributes

trait GraphPart {
	var id : Option[Int] = None
	
	/**
   * Identifier of this node, must be set to enable storing and loading...
   */

  def setId(i : Int) = id = Some(i)
  
  def getId  : Int = id match {
	  case None => {id = Some(UniqueId.newid()) ; getId}
	  case Some(x) => x
	}
  
	def setAttribute[T](key : String, value: T){
	  Attributes.setAttribute[T](key,this.asInstanceOf[GraphPart],value)
	}
	
	def getAttribute[T](key: String) : T = {
	  Attributes.getAttribute[T](key).get(this)
	}
}