package applications.virus
import networkframework.core.engine.TurnBasedEngine
import networkframework.core.io.Log
import networkframework.core.engine.RoundBasedEngine
import networkframework.core.engine.EventBasedEngine
import networkframework.core.monitoring.Event
import networkframework.core.graph._
import networkframework.core.io._
import networkframework.core.engine._
import scala.util.Random
import networkframework.core.engine.SimulationUpdateEvent
import networkframework.core.statistic.Statistics
import networkframework.modules.visual._


/**
 * A sample application with a virus that can spread.
 * Actors can be vaccinated after which they can not get infected again
 * Actors can die when they are weak and infected
 * Actors can resist an infection
 * Actors can spread an infection depending on how likely their bond with another actor is
 * 
 * Eg a disease in a social network
 */
class VirusNode extends BasicNode{
  
  /** extra convenient constructors */
  def this(inf:Boolean, res:Double, spr:Double, scared:Double, weak: Double, vaccinated: Boolean) = {this(); setAttribute("scared",scared); setAttribute("weakness",weak); setAttribute("vaccinated",vaccinated); setAttribute("infected",inf);setAttribute("resistance",res); setAttribute("spread",spr)}
  def this(inf:Boolean) = {this();setAttribute("infected",inf);setAttribute("resistance",random.nextDouble); setAttribute("spread",random.nextDouble); setAttribute("vaccinated",false); setAttribute("weakness",random.nextDouble); setAttribute("scared",random.nextDouble)}
  
  /** A random generator */
  private val random = new Random
  

  
  /** If this method is called the virus reaches this node. Depending on the resistance the node will be infected */
  def infect = {
    Log.log("[ViralNode "+id+"] receiving infection")
    if (random.nextDouble > getAttribute[Double]("resistance"))  {
      setAttribute("infected",true)
      Log.log("[ViralNode "+id+"] infected!")
    }else{
     Log.log("[ViralNode "+id+"] resisted the infection")
    }
  }
  
  def vaccinate = {
    Log.log("[ViralNode "+id+"] receiving vaccine")

      setAttribute("infected",false)
      setAttribute("resistance",1.0)
      setAttribute("vaccinated",true);
      Log.log("[ViralNode "+id+"] vaccinated!")
    
  }
  
  /**
   * The 'act' method of the Node. This must be implemented by every custom node.
   * A command is always sent to the simulation engine. It will then decide when to execute the command.
   * This allows for different types of simulations to run.
   * 
   * This node first decides whether or not to try to infect another node. 
   * If he will, he generates a command and sends it to the engine.
   */
  override def act() = {
    random : Random;
    // if this node is vaccinated, nothing can happen to it
    if(!getAttribute[Boolean]("vaccinated")){
      
          //if this node is scared, it might get a vaccination
      if (getAttribute[Double]("scared") * random.nextDouble() > 0.4){
          new VaccinateCommand(this).send;
      }
      
      //if this node is infected it can either die or pass the infection
      else if(getAttribute[Boolean]("infected")){
        
        //weaker persons die faster
      	if(getAttribute[Double]("weakness") * random.nextDouble() > 0.5){
          new Die(this).send;
        }else{
        	for (e <- getEdges) yield (
      		  e match {
      		    case i : InfectionEdge => if (!i.destination.getAttribute[Boolean]("infected") & !i.destination.getAttribute[Boolean]("vaccinated") & random.nextDouble < getAttribute[Double]("spread")) (new InfectionCommand(i)).send 
      		    case _ => Unit
      		  });
        }
      }
    }

  }
}

/**
 * A specific type of edge for a viral network.
 */
class InfectionEdge(a: VirusNode, b:VirusNode) extends UnidirectionalEdge(a,b) {
  /** The source of infection */
  val source = a
  /** The destination of the infection */
  val destination = b
  

  /** Convenient constructor */
  def this(a:VirusNode, b:VirusNode, d : Double) = {this(a,b); setAttribute("susceptibility",d)}
  
}

/**
 * A command specifying what to do when a node decides to try to infect another node.
 */
class InfectionCommand(i : InfectionEdge) extends Command {
  /** The edge on which this command is executed */
  val edge : InfectionEdge = i
  
  /** A random generator */
  val random = new Random
  
  /** 
   * The execute method must be implemented by each command.
   * In this case the command will try to infect the other node 
   */
  override def execute = {
      Log.log("[InfectionCommand] " + i.source.id + " --> " + i.destination.id )
      if (!i.destination.getAttribute[Boolean]("infected") & random.nextDouble < (i.getAttribute[Double]("susceptibility")))
        i.destination.infect
      else
          Log.log("[InfectionCommand] " + i.source.id + " --> " + i.destination.id +" not applied")
    }
}

/**
 * A command specifying what to do when a node decides to try to infect another node.
 */
class VaccinateCommand(n : VirusNode) extends Command {

  
  /** 
   * The execute method must be implemented by each command.
   * In this case the command will try to infect the other node 
   */
  override def execute = {
      Log.log("[VaccinateCommand] " + n.id)
      n.vaccinate
    }
}


/**
 * Creation of an actual viral network
 */
class VirusNetwork {
  /** create the nodes */
  val n0 = new VirusNode(true, 0.8, 0.2,0.4,0.5,false)
  val n1 = new VirusNode(false)
  val n2 = new VirusNode(false)
  val n3 = new VirusNode(true)
  val n4 = new VirusNode(false)
  
  /** create the graph */
  val list : List[VirusNode] = List(n0,n1,n2,n3,n4)
  val graph : Graph = new BasicGraph(list)
  
  /** create the edges */
  graph.add(new InfectionEdge(n0,n1,0.1))
  graph.add(new InfectionEdge(n0,n2,0.9))
  graph.add(new InfectionEdge(n1,n2,0.5))
  graph.add(new InfectionEdge(n2,n1,0.5))
  graph.add(new InfectionEdge(n1,n3,0.5))
  graph.add(new InfectionEdge(n1,n4,0.5))

  
  
}