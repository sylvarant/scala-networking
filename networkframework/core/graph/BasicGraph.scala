package networkframework.core.graph
import networkframework.core.engine._
import scala.util.Random


/**
 * Node generic graph class
 */
class BasicGraph extends Graph{
  /** 
   * Convenient constructor 
   * (also sets IDs) 
   */
  def this(l : List[Node]) { this() ;var i = 0; for (n <- l) yield {add(n)}}

}
