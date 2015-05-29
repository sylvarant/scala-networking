package applications.socialvirus.statistic
import networkframework.core.statistic.Statistics
import networkframework.core.statistic.Statistic
import networkframework.core.graph.Node
import networkframework.core.algorithm.SortNodes
import networkframework.core.monitoring.Event
import networkframework.core.graph.attribute.AttributeXUpdateEvent

/**
 * Statistic for the percentage infected actors
 */
case class PercentageInfected extends Statistic[Float]{
  
  key = "percentageInfected"
  runOn += "attribute.InfectedUpdate"
  runMethod = this.calculate;
  
  override def calculate(e: Event){
    if(Statistics.engine.running){
      var totalInfected : Int = 0;
      var total : Int = 0;
      Statistics.engine.graph.foreach((n : Node) => {total +=1; if(n.getAttribute[Boolean]("infected")) totalInfected += 1})
      value = totalInfected/total * 100;
      super.calculate(e);
    }
  }
}

