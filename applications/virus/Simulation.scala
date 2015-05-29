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
import networkframework.modules.visual.VisualCoreModule
import networkframework.modules.visual.ubigraph.UbigraphModule
import applications.virus.statistic._
import applications.Application
import networkframework.core.graph.attribute.AttributeXUpdateEvent
import networkframework.core.statistic.StatisticXUpdatedEvent
import networkframework.core.graph.attribute.AttributeXUpdateEvent
import networkframework.modules.basestats.TotalRunningTime

/**
 * A sample application with a virus that can spread.
 * Actors can be vaccinated after which they can not get infected again
 * Actors can die when they are weak and infected
 * Actors can resist an infection
 * Actors can spread an infection depending on how likely their bond with another actor is
 * 
 * Eg a disease in a social network
 */

abstract class VirusSimulation extends Application {
  
  name = "Turn-based Virus Simulation"
  description = "A turn based simulation of a virus network, with visualization, monitoring & statistics in place. Each node has a certain chance to die, get infected, spread infection, get a vaccination, ... Make sure you have ubigraph running at: http://127.0.0.1:20738/RPC2.This simulation waits 200ms between each step so the visualization is clear"
    
  var network : VirusNetwork = null
  var graph : Graph = null
  
  def init = {
  
  /** Adding modules */
  engine.installModule(VisualCoreModule);
  engine.installModule(new UbigraphModule("http://127.0.0.1:20738/RPC2","#ffffff"));
  engine.installModule(VirusModule);
  
  engine.sleep = 100;
  
  /** The actual graph */
  network = new VirusNetwork
  graph = network.graph
  
  engine.graph = graph;
  
	
  } 
  
  def start = engine.start
	
}

class TurnBasedVirusSimulation extends VirusSimulation {
  
  name = "Turn-based Virus Simulation"
  description = "A turn based simulation of a virus network, with visualization, monitoring & statistics in place. Each node has a certain chance to die, get infected, spread infection, get a vaccination, ... Make sure you have ubigraph running at: http://127.0.0.1:20738/RPC2.This simulation waits 200ms between each step so the visualization is clear"
    

  override def init = {
  /** setting up the turn based engine */
  _engine = Some(new TurnBasedEngine)
  super.init;
  } 
  
  override def start = {
	  /** Some prints to show what is happening */
    println("Starting turnbased simulation of Viral Network")
    engine.start
    println("Finished turnbased simulation.")  
  }
	
}

class RoundBasedVirusSimulation extends VirusSimulation {
  name = "Round-based Virus Simulation"
  description = "A round based simulation of a virus network, with visualization, monitoring & statistics in place. Each node has a certain chance to die, get infected, spread infection, get a vaccination, ... Make sure you have ubigraph running at: http://127.0.0.1:20738/RPC2.This simulation waits 200ms between each step so the visualization is clear"
  
   override def init = {

  /** setting up the round based engine */
  _engine = Some(new RoundBasedEngine)
  super.init
  
  }
  
  override def start = {
	  /** Some prints to show what is happening */
    println("Starting roundbased simulation of Viral Network")
    engine.start
    println("Finished roundbased simulation.")  
  }
  
}

class EventBasedVirusSimulation extends VirusSimulation{
  
  name = "Event-based Virus Simulation"
  description = "An event based simulation of a virus network, with visualization, monitoring & statistics in place. Each node has a certain chance to die, get infected, spread infection, get a vaccination, ... Make sure you have ubigraph running at: http://127.0.0.1:20738/RPC2.This simulation waits 200ms between each step so the visualization is clear"
    
  override def init = {
  
  _engine = Some(new EventBasedEngine)
  super.init;
  /**
   * Some alterations to make use of the event functionality
   */
  network.n0.setAttribute("infected",false);
    /** dispatch the event to the engine */
  (new InfectionEvent(network.n0,5)).send
  }
  /**
   * A simple Event that takes place at a point in time t, and that will infect the node
   */
  class InfectionEvent(n : VirusNode, t : Integer) extends networkframework.core.engine.Event(t) {
    def execute = n.setAttribute("infected",true);
  }

  
  override def start = {
	  /** Some prints to show what is happening */
    println("Starting eventbased simulation of Virus Network")
    engine.start
    println("Finished eventbased simulation.")  
  }
  
}

/**
 * Connect virus simulation with the ubigraph properties
 */
object UbigraphBridge{
  
  /**
   * Change color depending on infected/vaccinated
   */
  def checkNodeCondition(e : Event){
    e match{
      case AttributeXUpdateEvent(node, attr, value) =>  
        if(attr.equals("vaccinated") && value.asInstanceOf[Boolean])
        node.setAttribute("Ubigraph.node.color","#B40404")
        else if(attr.equals("infected") && value.asInstanceOf[Boolean])
        node.setAttribute("Ubigraph.node.color","#3ADF00")
      case _ =>
    }
  }
 
  
  def checkEdgeCondition(e : Event){
    e match{
      case AttributeXUpdateEvent(node, attr, value) =>  
        if(attr.equals("vaccinated"))
        node.setAttribute("Ubigraph.node.color","#B40404")
        else if(attr.equals("infected") && value.asInstanceOf[Boolean])
        node.setAttribute("Ubigraph.node.color","#3ADF00")
      case _ =>
    }
  }
  
}


/**
 * Stop conditions
 */
object SocialBridge{
  /**
   * Render network on new simulation
   */
  def checkCondition(e : Event){
    e match{
      case StatisticXUpdatedEvent(stat,engine) => stat match {
        case y: LivingActors => if(y.value == 100) { 
          engine.graph.getNodes.filter(n => (n.getId < 3)).foreach(n => n.setAttribute("infected",true)) 
         };
      }
      case _ =>
    }
  }
  
}


/**
 * Stop conditions
 */
object StopConditions{
  /**
   * Render network on new simulation
   */
  def checkCondition(e : Event){
    e match{
      case StatisticXUpdatedEvent(stat,engine) => stat match {
        case x: PercentageVaccinized => if(x.value == 100.0) engine.running = false;
        case y: LivingActors => if(y.value == 0) engine.running = false;
        case z: TotalRunningTime => if(z.value >= 300000) engine.running = false;
      }
      case _ =>
    }
  }
  
}


