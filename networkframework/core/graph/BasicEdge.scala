package networkframework.core.graph

/**
 * Edge class
 */
abstract class BasicEdge(val l : Node,val r : Node) extends Edge{
  val leftNode = l
  val rightNode = r
  
  override def left = leftNode
  override def right = rightNode
  
}

/**
 * Bidirectional Edge
 */
case class BidirectionalEdge(override val left : Node,override val right : Node)  extends BasicEdge(left,right){
  edgeType = "bidirectional"

} 

/**
 * Unidirectional Edge
 */
case class UnidirectionalEdge(override val left : Node,override val right : Node)  extends BasicEdge(left,right){
  edgeType = "unidirectional"

} 

