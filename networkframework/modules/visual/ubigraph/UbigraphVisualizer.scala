package networkframework.modules.visual.ubigraph

import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import networkframework.modules.visual.Visualizer
import networkframework.core.monitoring.Event
import networkframework.core.graph.attribute._
import networkframework.core.graph._
import networkframework.core.graph.attribute.AttributeXUpdateEvent
import org.ubiety.ubigraph.UbigraphClient

/**
 * Ubigraph Visualizer Implementation
 */
class UbigraphVisualizer(url : String) extends Visualizer{
 
  var ubigraph : UbigraphClient = new UbigraphClient();
  
  
  def updateNodeAttribute(e: Event){
    e match{
      case AttributeXUpdateEvent(node,key,value) => ubigraph.setVertexAttribute(node.getId,"Ubigraph.node.".r.replaceFirstIn(key,""),value.toString())
      case _ =>
    }
  }
  
  def updateEdgeAttribute(e: Event){
    e match{

      case AttributeXUpdateEvent(edge,key,value) => ubigraph.setEdgeAttribute(edge.getId,"Ubigraph.edge.".r.replaceFirstIn(key,""),value.toString())
      case _ =>
    }
  }
  

  def addNode(n : Node){
  	ubigraph.newVertex(n.getId);
  	
  	ubigraph.setVertexAttribute(n.getId,"color",n.getAttribute[String]("Ubigraph.node.color"));
  	ubigraph.setVertexAttribute(n.getId,"fontcolor",n.getAttribute[String]("Ubigraph.node.fontcolor"));
  	ubigraph.setVertexAttribute(n.getId,"shape",n.getAttribute[String]("Ubigraph.node.shape"));
  	ubigraph.setVertexAttribute(n.getId,"label",n.getAttribute[String]("Ubigraph.node.label"));
  	ubigraph.setVertexAttribute(n.getId,"size",n.getAttribute[Double]("Ubigraph.node.size").toString());
  }
  
  def addEdge(e : Edge){
    ubigraph.newEdge(e.getId , e.left.getId ,e.right.getId );
      	
  	ubigraph.setEdgeAttribute(e.getId,"color",e.getAttribute[String]("Ubigraph.edge.color"));
  	ubigraph.setEdgeAttribute(e.getId,"fontcolor",e.getAttribute[String]("Ubigraph.edge.fontcolor"));
  	ubigraph.setEdgeAttribute(e.getId,"arrow",e.getAttribute[Boolean]("Ubigraph.edge.arrow").toString());
  	ubigraph.setEdgeAttribute(e.getId,"spline",e.getAttribute[Boolean]("Ubigraph.edge.spline").toString());
  	ubigraph.setEdgeAttribute(e.getId,"width",e.getAttribute[Double]("Ubigraph.edge.width").toString());
  	ubigraph.setEdgeAttribute(e.getId,"label",e.getAttribute[String]("Ubigraph.edge.label"));
  }
  
  def removeNode(n : Node){
    ubigraph.removeVertex(n.getId );
  }
  
  def removeEdge(e: Edge){
  	ubigraph.removeEdge(e.getId);
  }
  
  def clear(){
  	ubigraph.clear();
  }
	

}