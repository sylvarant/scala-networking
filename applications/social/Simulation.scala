package applications.social
import networkframework.core.engine.EventBasedEngine
import networkframework.core.engine.StepTerminator
import networkframework.core.io.Log


import networkframework.core.engine.Module.Module
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.Node
import networkframework.core.graph.Edge
import applications.virus.statistic._



object SocialModule extends Module{
	
  key = "SocialNetwork";
  //requirements += "Ubigraph"
    
  attributes += new Attribute[Node,String]("name",""); //name of actor
  attributes += new Attribute[Node,SocialNetwork]("sn",null); //social network of actor
  attributes += new Attribute[Node,Double]("attractiveness",0.1); //attractiveness of actor

}


class SocialSimulation extends applications.Application{
  
  name = "Social Simulation"
  description = "A simulation of a social network in which 2 actors enter after a while. The dynamics are represented as they try to make friends. An Eventbased engine is used."
  
  var model : SocialNetworkModel = null
  var network : SocialNetwork = null
    
  def init = {
     
 /** create a simulation engine. We use event based so we can add new actors in the middle of the simulation */
  _engine = Some(new EventBasedEngine)
  engine.installModule(SocialModule);
  
  model = new SocialNetworkModel
  network = model.network
  engine.graph = network;
  
  /** create an event in which an ugly guy enters */
  val uglyguy = new Male("Ugly Guy", 0.0, network)
  (new EntryEvent(uglyguy,network, 5)).send
  
  /** And lets add a pretty guy */
  val prettyguy = new Male("Pretty Guy", 1.0, network)
  (new EntryEvent(prettyguy, network, 5)).send
  
  /** create a stop condition */
  val stop = new StepTerminator(30, engine)
  engine.addTerminator(stop)
  
  }
  
  def start = {
	  /** Run the simulation */
	  println("Running Social Network simulation");
	  println("Boys : " + network.boys.size)
	  println("Girls : " + network.girls.size)
	  engine.start
	  println("Finished Social Network simulation");

	  /** Print the final state of the actors */
	  for (b <- network.boys) yield (println(b.name + " has " + b.neighbours.size + " friends."))
	  for (g <- network.girls) yield (println(g.name + " has " + g.neighbours.size + " friends."))
  }
}