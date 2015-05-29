package applications.roads
import networkframework.core.graph.messaging._
import networkframework.core.io.Log
import networkframework.core.graph.BasicGraph
import networkframework.core.engine._

/**
 * A custom graph, that holds no extra functionality right now.
 */
class RoadMap extends BasicGraph {
  
}


/**
 * A city
 * 
 * The overloading of these methods should be so that you can use Vehicle as parameter, however the most funky 
 * generics did not solve this problem.
 */
class City extends LIFOMessagingNode {
  
  def this(name : String){
    this();
    setAttribute("name",name);
  }
  
  def name:String = getAttribute[String]("name");
  
  /**
   * What to do when a car arrives in the city?
   */
  override def receive(v : Packet) = {

        Log.log("Car entering " + name + ".")
        super.receive(v)

  }
  
  /**
   * What to do when a car reaches his destination here?
   */
  override def process(p: Packet) {
	  Log.log("Car arrived in " + name + "!" )
  }
  
  
}

/**
 * Roads that connect cities
 */
class Road(a : City, b : City) extends BidirectionalMessagingEdge(a,b) {
  override val left = a
  override val right = b
  
}

/**
 * Vehicles that wish to drive from one city, to another
 */
class Vehicle(a : City, b : City) extends Message("nothing", a,b) {

  /**
   * This method is called when a vehicle is sent over a Road to another City
   */
  override def forward(e : MessagingEdge) {
    super.forward(e)
    
	var from = "unknown"
    var to = "unknown"
    
    e match {
      case r : Road => if ( r.left == current) {from = r.left.name; to = r.right.name} else { from = r.right.name; to = r.left.name}
      case _ => Unit
    }
	
	Log.log("leaving " + from + " for " + to + ".")

  }
}

/**
 * A terminating condition
 */
class Arrival(val car : Vehicle) extends Terminator {
  override def stop = {car.current == car.destination}
}

/**
 * The complete model of the road network
 */
class RoadNetworkModel {
  
  val map = new RoadMap
  
  val brussels = new City("Brussels"); 	map.add(brussels)	
  val antwerp = new City("Antwerp");	map.add(antwerp)
  val bruges = new City("Bruges");		map.add(bruges)
  val leuven = new City("Leuven");		map.add(leuven)
  val ghent = new City("Ghent");		map.add(ghent)
  val liege = new City("Liege");		map.add(liege)
  
  val car = new Vehicle(leuven, antwerp)

  brussels.addEdge(new Road(brussels, antwerp))
  brussels.addEdge(new Road(brussels, ghent))
  brussels.addEdge(new Road(brussels, leuven))
  leuven.addEdge(new Road(leuven, liege))
  antwerp.addEdge(new Road(antwerp, ghent))
  ghent.addEdge(new Road(ghent, bruges))
 
}