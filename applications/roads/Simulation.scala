package applications.roads
import networkframework.core.engine._
import networkframework.core.io.Log
import networkframework.core.graph.messaging._

import networkframework.core.engine.Module.Module
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.Node
import networkframework.core.graph.Edge
import applications.virus.statistic._



object RoadNetworkModule extends Module{
	
  key = "RoadNetwork";
  attributes += new Attribute[Node,String]("name",""); //name of city

}

class RoadSimulation extends applications.Application{

  name = "Road Simulation"  
  description = "A round based simulation of a city network with some roads, in which a single car attempts to travel from Leuven to Antwerp. This purely demonstrates the messaging features of the framework."
  
  def init = {
    
    /** setting up the turn based engine */
  _engine = Some(new RoundBasedEngine)
  
  
  /** adding a terminating condition */
  engine.installModule(RoadNetworkModule);
  
    /** Loading the network model */
    val model = new RoadNetworkModel
    engine.graph = model.map
    
     /** Defining stop conditions */
     val stop = new Arrival(model.car)
     engine.addTerminator(stop)
  
     /** Initialize the engine */
     engine.setRouter(new RandomRouter)
  }
  
  /** Start */
  def start = {
    println("Starting simulation")
    engine.start
    println("Finishing simulation")
  }
}