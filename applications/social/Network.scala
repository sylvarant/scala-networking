package applications.social
import networkframework.core.algorithm.Algorithms
import networkframework.core.engine.Command
import networkframework.core.engine.Event
import networkframework.core.engine.EventBasedEngine
import networkframework.core.graph.BasicGraph
import networkframework.core.graph.BasicNode
import networkframework.core.graph.BidirectionalEdge
import networkframework.core.graph.Node
import networkframework.core.engine.StepTerminator
import networkframework.core.io.Log
import scala.util.Random

/**
 * A custom graph, representing the social network
 */
class SocialNetwork extends BasicGraph {

  /** Get the boys & girls */
  def girls : List[Female] = filter[Female]((n => n match { case _ : Female => true; case _ => false}))
  def boys : List[Male]= filter[Male]((n => n match { case _ : Male => true; case _ => false}))
  
  def randomGirl : Option[Female] = if (girls.size > 0) Some(girls(random.nextInt(girls.size-1))) else None
  def randomBoy : Option[Male] = if (girls.size > 0) Some(boys(random.nextInt(boys.size-1))) else None
}


/**
 * The actor in this dynamic network.
 */
abstract class Actor extends BasicNode {

  def this(name: String, attr:Double, sn: SocialNetwork) = {this();setAttribute("name",name); setAttribute("sn",sn); setAttribute("attractiveness",attr)}
  def this(name: String, sn: SocialNetwork) = {this(name,Random.nextDouble(), sn);}
  
  
  def name = getAttribute[String]("name");
  def sn = getAttribute[SocialNetwork]("sn");
  def attractiveness  = getAttribute[Double]("attractiveness");

  
  /** this actor decides whether or not to accept a friend request */
  def acceptFriend(other : Actor) = {
    /** how many nodes are there between us? */
    val d = Algorithms.minDist(other, this)
    /** if it's less than 3, we're good. So you have to be a friend of a friend */
//    Log.log("Distance between " + other.name + " and " + name + " is " + d)
    
    if (d <= 2 & d >= 0) true
    else false
    
  }

}

/**
 * A male
 */
class Male extends Actor {
  
  def this(name: String, attr:Double, sn: SocialNetwork) = {this();setAttribute("name",name); setAttribute("sn",sn); setAttribute("attractiveness",attr)}
  def this(name: String, sn: SocialNetwork) = {this(name,Random.nextDouble(), sn);}
  
  
  override def acceptFriend(other: Actor):Boolean = {
    (neighbours.size == 0) | super.acceptFriend(other)
  }
  
  /** A guy is likely to send out requests to any girl */
  override def act = {
    sn.randomGirl match {
      case Some(girl) => if (!neighbours.contains(girl)) (new FriendRequest(this, girl)).send
      case None => Unit
    }
  }
}

/**
 * A female
 */
class Female extends Actor {
  
  def this(name: String, attr:Double, sn: SocialNetwork) = {this();setAttribute("name",name); setAttribute("sn",sn); setAttribute("attractiveness",attr)}
  def this(name: String, sn: SocialNetwork) = {this(name,Random.nextDouble(), sn);}
  
  
  override def acceptFriend(other: Actor) = {
    super.acceptFriend(other) & (other match {
      case x : Male => (x.attractiveness >= attractiveness)
      case y : Female => true
      case _ => false
    }
    )
  }
  
  /** A girl is likely to invite handsome guys, and other girls */
  override def act = {
    sn.randomGirl match {
      case Some(girl) => if (girl != this & !neighbours.contains(girl))(new FriendRequest(this, girl)).send
      case None => Unit
    }
    
    sn.randomBoy match {
      case Some(male) => if (!neighbours.contains(male)) {(if (male.attractiveness > this.attractiveness) (new FriendRequest(this, male)).send)}
      case None => Unit
    }
  }
}

/**
 * A class depicting a friendship between 2 persons in a social network
 */
class Friendship(a:Actor, b:Actor) extends BidirectionalEdge(a,b) {
  Log.log("New Friendship : " + a.name + " <-> " + b.name);
}

/**
 * A friendship request, sent when one user wants to befriend another user
 */
class FriendRequest(a:Actor, b:Actor) extends Command {
  
  /** actor a requests actor b to become his or her friend */
  override def execute = {
    Log.log("Friend Request : " + a.name + " -> " + b.name)
    if(b.acceptFriend(a)) {
      a.addEdge(new Friendship(a,b))
    }
  }
}

/**
 * An event specifying an actor to enter the social network
 */
class EntryEvent(a:Actor, n:SocialNetwork, t:Integer) extends Event(t) {
  
  override def execute = {
    Log.log(a.name + " is entering the network...")
    n.add(a)
    }
  
}

class SocialNetworkModel {
  /** create the network */
  val network = new SocialNetwork
  
  /** Adding of the people happens in the constructor of the actors */
  val adri = new Male("Adriaan", 0.9, network); network.add(adri);
  val tom = new Male("Tom", 0.7, network); 		network.add(tom);
  val john = new Male("John", 0.5, network);	network.add(john);
  val mark = new Male("Mark", 0.2, network);	network.add(mark);
  
  val julie = new Female("Julie", 0.4, network);	network.add(julie);
  val elisha = new Female("Elisha", 0.8, network);	network.add(elisha);
  val ann = new Female("Ann", 0.5, network);		network.add(ann);
  val susan = new Female("Susan", 0.2, network);	network.add(susan);
  val betty = new Female("Betty", 0.3, network);	network.add(betty);

  /** create some friendships */
  adri.addEdge(new Friendship(adri,tom));
  adri.addEdge(new Friendship(adri,mark));
  
  john.addEdge(new Friendship(john,mark));
  john.addEdge(new Friendship(john,elisha));
  
  mark.addEdge(new Friendship(mark,tom));
  
  julie.addEdge(new Friendship(julie,elisha));
  julie.addEdge(new Friendship(julie,susan));
  
  ann.addEdge(new Friendship(ann,betty));
  ann.addEdge(new Friendship(ann,susan));
  ann.addEdge(new Friendship(ann,elisha));
   
}