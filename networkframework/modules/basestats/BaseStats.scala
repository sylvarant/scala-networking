package networkframework.modules.basestats


import networkframework.core.engine.Module._
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.Node
import networkframework.core.graph.GraphPart
import networkframework.core.statistic.Statistics
import networkframework.core.statistic.Statistic
import networkframework.core.graph.Node
import networkframework.core.monitoring.Event
import networkframework.core.engine.SimulationStartEvent
import networkframework.core.engine.SimulationUpdateEvent
import networkframework.core.monitoring.Event

/**
 * Some basic statistics
 */
class BaseStatsModule extends Module{
	
  key = "BaseStats";
	
	var rt : TotalRunningTime = new TotalRunningTime;

	statistics += rt
}




/**
 * Statistic for how many actors are alive
 */
case class TotalRunningTime extends Statistic[Long]{
  
  key = "runningTime"
    
  var startTime : Long = 0
  runOn += "engine.simulationStart"
  runOn += "engine.update"
  runMethod = this.calculate;

  
  override def calculate(e: Event){
    e match{
      case SimulationStartEvent(e) => value = 0; startTime = System.currentTimeMillis();
      case SimulationUpdateEvent(e) => value = System.currentTimeMillis() - startTime;
      case _ =>
    }
      super.calculate(e);
    
  }
}