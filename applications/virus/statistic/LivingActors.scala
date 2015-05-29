package applications.virus.statistic
import networkframework.core.statistic.Statistics
import networkframework.core.statistic.Statistic
import networkframework.core.graph.Node
import networkframework.core.algorithm.SortNodes
import networkframework.core.monitoring.Event
import networkframework.core.graph.attribute.AttributeXUpdateEvent
import networkframework.core.graph.RemoveNodeEvent

/**
 * Statistic for how many actors are alive
 */
case class LivingActors extends Statistic[Int]{
  
  key = "livingActors"
  runOn += "graph.removeNode"
  runMethod = this.calculate;
  
  override def calculate(e: Event){
    if(Statistics.engine.running){
      value = 0;
      Statistics.engine.graph.foreach((n : Node) => {value +=1;})
      super.calculate(e);
    }
  }
}