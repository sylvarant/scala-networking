package networkframework.core.io
import scala.xml.PrettyPrinter
import scala.xml.XML
import networkframework.core.graph.Graph
import networkframework.core.graph.Node

/**
 * This class can handle standard IO.
 * 
 * It has never been finished though.
 */
object IO{  
  /**
   * to print more pretty XML - does not work though
   */
  val printer = new PrettyPrinter(100,4)
  
  /**
   * Save a graph to a filename
   */
  def save(graph : Graph, file : String) = {
	 //val xml = graph.toXml
	  //scala.xml.XML.save(file+".xml", xml)
	  
  }
  
  
  /**
   * Load an XML file. The user has to specify a method to construct nodes, and the user also has to specify one to construct edges
   * from XML...
   * In both cases the <node ..> <edge ... /> </node> tags will be given to the method 
   */
  def load[B <: Node](file : String, constructNode: (scala.xml.NodeSeq => B ), constructEdge: ((Graph,scala.xml.NodeSeq) => B )) = {
//    val xml = scala.xml.XML.loadFile(file)
////    val graph = new Graph
//    
//    for (node <- xml \ "graph" \ "node") {
//      graph.add(constructNode(node))
//    }
//    
//    for (node <- xml \ "graph" \ "node") {
//      constructEdge(graph, node)
//    }
//    graph
  }
  
  
  
//  /**
//   * Load a graph from a file
//   * extract(Graph, NodeSeq) is a method that extracts additional custom defined data from the XML NodeSequence
//   */
//  def load[T <: Node](file : String, extract: ((Graph, scala.xml.NodeSeq) => Unit) = (_, _) => Nil)
//  (implicit man : Manifest[T]) = {
//    
//    /**
//     * ugly work around because you can't do 'new T' apparantly
//   	 */	
//    def getOne: T = man.erasure.newInstance.asInstanceOf[T]
//    
//    val xml = scala.xml.XML.loadFile(file)
//    val graph = new Graph
//    
//    val nrNodes : Int = Integer.parseInt((xml \ "graph" \ "@nodes").text)
//    
//    /** first get all the nodes */
//    for (node <- xml \ "graph" \ "node") {
//      val id = (node \ "@id")
//      val n = getOne
//      n.setId(Integer.parseInt(id.text))
//      graph.add(n)
//    }
//    
//    /** Then get all the special node properties. For now let's assume edges are given by the indices... */
//    for (node <- xml \ "graph" \ "node") {
//      for (edge <- node \ "edge") {
//        val left = graph.get(Integer.parseInt((edge \ "@left").text))
//        val right = graph.get(Integer.parseInt((edge \ "@right").text))
//        (edge \ "@type").text match {
//          case "unidirectional" => left.addEdge(new UnidirectionalEdge(left,right))
//          case "bidirectional" => left.addEdge(new BidirectionalEdge(left,right))	// bidirectionality caught in the addEdge method
//        }
//      }
//    }
//    
//    extract(graph, xml)
//  }
}
