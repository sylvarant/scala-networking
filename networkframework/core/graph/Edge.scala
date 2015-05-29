package networkframework.core.graph

trait Edge extends GraphPart{
	var edgeType = "undirected"
  
	/**
	 * possible weight or other value type
	 * can be easily inherited and changed
	 */
	def value : Option[Any] = None
	
	def accept(visitor : Visitor) = visitor.visit(this)
	
	/**
	 * Returns the left node
	 */
	def left : Node
	
	/**
	 * Returns the right node
	 */
	def right : Node
	
	/** Remove this edge from the graph */
	def remove: Unit = Unit
	
	/** 
	 * default processing method of this edge.
	 * Useful for messaging edges, but also for edges that can undergo changes 
	 */
	
	def process: Unit = Unit
}

