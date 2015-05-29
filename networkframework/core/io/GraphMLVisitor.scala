package networkframework.core.io
import networkframework.core.graph._

import scala.collection.immutable.Nil

class GraphMLVisitor extends IOVisitor{
  def content = Nil;
 
 def visit(g : Graph) = {
   def content = <graph id="G" edgedefault=""></graph>;//{g.nodes.map(_.visit(this))}</graph>;
 }
 
 def visit(n : Node){
   def content = <node id="{ n.id }"/>;
 }
 
 def visit(e: Edge){
   def content = <edge source="{ left.id }" target="{ right.id }" directed="true/false" />
 }
 
 override def save(destination : String){
   <?xml version="1.0" encoding="UTF-8"?>
	 <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns
     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
	 </graphml>
 }
}