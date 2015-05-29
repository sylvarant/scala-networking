package applications.socialvirus.statistic
import networkframework.core.statistic.Statistics
import networkframework.core.statistic.Statistic
import networkframework.core.monitoring.Event
import networkframework.core.graph.Node
import networkframework.core.io.Log


/**
 * Statistic for how many actors are alive
 */
case class LivingActors extends Statistic[Int]{
  
  key = "livingActors"
  runOn += "graph.removeNode"
  runOn += "graph.newNode"
  runMethod = this.calculate;
  
  override def calculate(e: Event){
    if(Statistics.engine.running){
      value = 0;
      Statistics.engine.graph.foreach((n : Node) => {value +=1;})
      Log.log("LIVING ACTORS = "+value.toString());
      super.calculate(e);
    }
  }
}