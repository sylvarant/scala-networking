package applications.socialvirus
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
import scala.collection.mutable.Queue


object Config {
  /** The chance people will procreate */
  val fertility = 0.1	
  
  /** The chance girls will make girl friends */
  val girlfriending = 0.2
  
  /** The chance that guys make male friends */
  val malefriending = 0.2
}



/**
 * A graph representing a network of real life social connections
 */
class SocialGraph extends BasicGraph {
    /** Get the boys & girls */
  def girls : List[Female] = filter[Female]((n => n match { case _ : Female => true; case _ => false}))
  def boys : List[Male]= filter[Male]((n => n match { case _ : Male => true; case _ => false}))
  
  def randomGirl : Option[Female] = if (girls.size > 1) Some(girls(random.nextInt(girls.size-1))) else { if (girls.size == 1) Some(girls(0)) else None }
  def randomBoy : Option[Male] = if (boys.size > 1) Some(boys(random.nextInt(boys.size-1))) else { if (boys.size == 1) Some(boys(0)) else None }
}

/**
 * A sample application with a virus that can spread.
 * Actors can be vaccinated after which they can not get infected again
 * Actors can die when they are weak and infected
 * Actors can resist an infection
 * Actors can spread an infection depending on how likely their bond with another actor is
 * 
 * Eg a disease in a social network
 */
abstract class Actor extends BasicNode{
  
  
  
  /** extra convenient constructors */
  def this(name:String, g: SocialGraph, inf:Boolean, res:Double, spr:Double, scared:Double, weak: Double, vaccinated: Boolean, attr: Double) = {this();   setAttribute("Ubigraph.node.label",name); setAttribute("name",name);setAttribute("sn",g);setAttribute("scared",scared); setAttribute("weakness",weak); setAttribute("vaccinated",vaccinated); setAttribute("infected",inf);setAttribute("resistance",res); setAttribute("spread",spr); setAttribute("attractiveness", attr);}
  def this(name:String, g: SocialGraph, inf:Boolean) = {this(name,g,inf,Random.nextDouble,Random.nextDouble,Random.nextDouble,Random.nextDouble,false,Random.nextDouble);}
  
  /** A random generator */
  private val random = new Random
  
  def name = getAttribute[String]("name")
  def sn = getAttribute[SocialGraph]("sn")
  def attractiveness = getAttribute[Double]("attractiveness")
  

  
  
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
    handleDisease
    makefriends
    procreate
  }
  
  def handleDisease = { 
    random : Random;
      //if this node is infected it can either die, get a vaccination or pass the infection
      if(getAttribute[Boolean]("infected")){
        
        //if this node is scared, it might get a vaccination
        if (getAttribute[Double]("scared") * random.nextDouble() > 0.4){
            new VaccinateCommand(this).send;
        }
          
        //weaker persons die faster
        else if(getAttribute[Double]("weakness") * random.nextDouble() > 0.5){
          new Die(this).send;
        }
        
        //or spread the infection
        else{
        	for (e <- getEdges) yield (
      		  e match {
      		    case i : InfectionEdge => if (!i.destination.getAttribute[Boolean]("infected") & !i.destination.getAttribute[Boolean]("vaccinated") & random.nextDouble < getAttribute[Double]("spread")) (new InfectionCommand(i)).send 
      		    case _ => Unit
      		  });
        }
      
    }
  }
  
  /** A function to attempt procreation */
  def procreate
  
  /** A person will try to befriend people from the same gender every now and then */
  def makefriends
}

/**
 * Represents a male actor.
 */
class Male extends Actor {
  val random = new Random
  
  /** extra convenient constructors */
  def this(name:String, g: SocialGraph, inf:Boolean, res:Double, spr:Double, scared:Double, weak: Double, vaccinated: Boolean, attr: Double) = {this(); setAttribute("Ubigraph.node.label",name);setAttribute("Ubigraph.node.fontcolor","#0000ff"); setAttribute("name",name);setAttribute("sn",g);setAttribute("scared",scared); setAttribute("weakness",weak); setAttribute("vaccinated",vaccinated); setAttribute("infected",inf);setAttribute("resistance",res); setAttribute("spread",spr); setAttribute("attractiveness", attr);}
  def this(name:String, g: SocialGraph, inf:Boolean) = {this(name,g,inf,Random.nextDouble,Random.nextDouble,Random.nextDouble,Random.nextDouble,false,Random.nextDouble);}
  
  /** A guy will select a random girl to procreate with */
  def procreate = {
	  if (random.nextDouble() < Config.fertility) {
		  var girl = sn.randomGirl
		  girl match {
		  case Some(x) => if (x.attractiveness > (attractiveness - 0.3)) (new MatingRequest(this, x, sn)).send
		  case None => Unit
		  }
	  }
  }
  
  /** Make some guy friends */
  def makefriends = {
    if (random.nextDouble() < Config.malefriending) {
    	sn.randomBoy match {
      		case Some(x) =>if (!isNeighbour(x) && x != this) {this.addEdge(new InfectionEdge(this, x)); x.addEdge(new InfectionEdge(x, this))} 
      		case None => Unit
    	}
    }
  }
  
}

/**
 * A class representing a female actor
 */
class Female extends Actor {
  val random = new Random
  
  var requests : Queue[MatingRequest] = new Queue[MatingRequest]
  
  /** extra convenient constructors */
  def this(name:String, g: SocialGraph, inf:Boolean, res:Double, spr:Double, scared:Double, weak: Double, vaccinated: Boolean, attr: Double) = {this(); setAttribute("Ubigraph.node.label",name);setAttribute("Ubigraph.node.fontcolor","#ff0080"); setAttribute("Ubigraph.node.shape","octahedron"); setAttribute("name",name);setAttribute("sn",g);setAttribute("scared",scared); setAttribute("weakness",weak); setAttribute("vaccinated",vaccinated); setAttribute("infected",inf);setAttribute("resistance",res); setAttribute("spread",spr); setAttribute("attractiveness", attr);}
  def this(name:String, g: SocialGraph, inf:Boolean) = {this(name,g,inf,Random.nextDouble,Random.nextDouble,Random.nextDouble,Random.nextDouble,false,Random.nextDouble);}
  
  def procreate = {
    if (requests.size > 0) {
    	val req : MatingRequest = requests.dequeue()
    	/** Decide to sleep with this guy or not, lets give the guy 50/50 */
    	if (random.nextBoolean) req.confirm
    }
  }
  
  /** Make some girl friends */
  def makefriends = {
    if (random.nextDouble() < Config.girlfriending) {
    	sn.randomGirl match {
      		case Some(x) => if (!isNeighbour(x) && x != this) {this.addEdge(new InfectionEdge(this, x)); x.addEdge(new InfectionEdge(x, this))} 
      		case None => Unit
    	}
    }
  }
}


/**
 * A specific type of edge for a viral network.
 * (an edge along which an infection can travel)
 */
class InfectionEdge(a: Actor, b:Actor) extends UnidirectionalEdge(a,b) {
  /** The source of infection */
  val source = a
  /** The destination of the infection */
  val destination = b
  

  /** Convenient constructor */
  def this(a:Actor, b:Actor, d : Double) = {this(a,b); setAttribute("susceptibility",d)}
  
}

/**
 * A class that resembles a Child -> Parent relationship 
 */
class Child(c: Actor, p: Actor) extends InfectionEdge(c,p) {
   setAttribute("Ubigraph.edge.arrow",true);
   setAttribute("Ubigraph.edge.color","#ff0000");
}

/**
 * A class that resembles a Parent -> Child relationship
 */
class Parent(p: Actor, c: Actor) extends InfectionEdge(p,c) {
  setAttribute("Ubigraph.edge.arrow",true);
  setAttribute("Ubigraph.edge.color","#ff0000");
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
class VaccinateCommand(n : Actor) extends Command {

  
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
 * A command initiated by a guy who would like to 'get at it' with some girl :).
 */
class MatingRequest(val guy : Male, val girl : Female, val graph: SocialGraph) extends Command {
  
  override def execute = {
    girl.requests.enqueue(this)
  }
  
  /** Confirm this 'mating request', and spawn a kid :) */
  def confirm = (new BirthCommand(guy, girl, graph)).send
}

/**
 * A command that creates a new child that enters the social network
 */
class BirthCommand(father: Male, mother: Female, graph: SocialGraph) extends Command {
  
  val random = new Random
  
  val malenames : List[String] = List("Philip", "Seba", "Tom", "Adriaan", "Dave", "Johnny", "Michael", "Stefan", "Felix", "Bruno", "Justin", "Luc", "Thomas", "Simon", "William");
  val femalenames : List[String] = List("Suzanne", "Maria", "Anne", "Linda", "Kate", "Sheryl", "Karen", "Ellen", "Rose", "Lindsay", "Sophie", "Evelyne", "Mary")
  
  /** Generate a new Child Actor, and add him or her to the network */
  override def execute = {
       
    var child : Actor = null
    if (random.nextBoolean) child = new Male(malenames(random.nextInt(malenames.size-1)), graph, false)
    else child = new Female(femalenames(random.nextInt(femalenames.size-1)), graph, false)    
    
    graph.add(child)
    child.addEdge(new Child(child,father))
    child.addEdge(new Child(child,mother))
    father.addEdge(new Parent(father, child))
    mother.addEdge(new Parent(mother, child))
    Log.log("[Birth] " + father.name + " and " + mother.name + " just had a baby, named " + child.name)
  }
}


/**
 * Creation of an actual viral network
 */
class VirusNetwork {
  /** The social graph */
  val graph : SocialGraph = new SocialGraph()
  
  /** create the nodes */
  val adam = new Male("Adam", graph, false, 1.0, 0.0, 0.0, 0.0, false, 1.0);
  val eve = new Female("Eve", graph, false, 1.0, 0.0, 0.0, 0.0, false, 1.0);
  
  /** create the graph */
  graph.add(adam)
  graph.add(eve)
  
}