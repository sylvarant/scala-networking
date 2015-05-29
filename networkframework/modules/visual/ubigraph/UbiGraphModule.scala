package networkframework.modules.visual.ubigraph

import networkframework.core.engine.Module._
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.Node
import networkframework.core.graph.GraphPart

/**
 * Module for Ubigraph Visualization
 */
class UbigraphModule(url : String, defaultColor : String) extends Module{
	
  key = "Ubigraph";
	
  requirements += "VisualCore";
	
	var ubi : UbigraphVisualizer = new UbigraphVisualizer(url);

	addHandler("attribute.Ubigraph.node.color.Update",ubi.updateNodeAttribute);
	addHandler("attribute.Ubigraph.node.label.Update",ubi.updateNodeAttribute);
	addHandler("attribute.Ubigraph.node.size.Update",ubi.updateNodeAttribute);
	addHandler("attribute.Ubigraph.node.shape.Update",ubi.updateNodeAttribute);
	
	addHandler("attribute.Ubigraph.edge.width.Update",ubi.updateEdgeAttribute);
	addHandler("attribute.Ubigraph.edge.color.Update",ubi.updateEdgeAttribute);
	addHandler("attribute.Ubigraph.edge.spline.Update",ubi.updateEdgeAttribute);
	addHandler("attribute.Ubigraph.edge.label.Update",ubi.updateEdgeAttribute);
	
	addHandler("graph.newNode",ubi.addNodeHandler);
	addHandler("graph.newEdge",ubi.addEdgeHandler);
	addHandler("graph.removeNode",ubi.removeNodeHandler);
	addHandler("graph.removeEdge",ubi.removeEdgeHandler);
	addHandler("engine.simulationStart",ubi.visualizeHandler);

	
	attributes += new Attribute[Node,String]("Ubigraph.node.color",defaultColor)
	attributes += new Attribute[Node,String]("Ubigraph.node.label","")
	attributes += new Attribute[Node,Double]("Ubigraph.node.size",1.0)
	attributes += new Attribute[Node,String]("Ubigraph.node.shape","cube")
	attributes += new Attribute[Node,String]("Ubigraph.node.fontcolor",defaultColor)
	
	attributes += new Attribute[Node,String]("Ubigraph.edge.color",defaultColor)
	attributes += new Attribute[Node,String]("Ubigraph.edge.label","")
	attributes += new Attribute[Node,Double]("Ubigraph.edge.width",1.0)
	attributes += new Attribute[Node,Boolean]("Ubigraph.edge.spline",true)
	attributes += new Attribute[Node,String]("Ubigraph.edge.fontcolor",defaultColor)
	attributes += new Attribute[Node,Boolean]("Ubigraph.edge.arrow",false)
	
}