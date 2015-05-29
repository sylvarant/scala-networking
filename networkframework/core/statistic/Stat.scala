package networkframework.core.statistic
import scala.collection.mutable.HashMap
import networkframework.core.monitoring.Monitor
import networkframework.core.engine.SimulationEngine
import networkframework.core.io.Log
import networkframework.core.monitoring.Event
import scala.collection.mutable.Set

object Statistics {
	val stats = HashMap.empty[String, Statistic[_ <: Any]]
	var engine : SimulationEngine = null
	
	def add(stat:Statistic[_ <: Any]) = {
	  stats += stat.key -> stat
	}
	
	def get(key: String):Statistic[_] = {
		stats.apply(key)
	}
	
}

trait Statistic[T]{
  var key : String = ""
  var timestamp : String = ""
  var value : T = _
  var history = HashMap[String, String]();
  var runOn : Set[String] = Set[String]();
  var runMethod : (Event) => Unit = this.run 
  
  def enable(){
    runOn.foreach(key => Monitor.bind(key, runMethod));
  }
  
  def disable(){
    runOn.foreach(key => Monitor.unbind(key, runMethod));
  }
  
  def run(e: Event){
    this.calculate(e);
    afterCalculation();
  }
  
  def calculate(e: Event) {afterCalculation()}
  
  def afterCalculation(){
    timestamp = Statistics.engine.step.toString()
   // history += timestamp -> value.toString();
    Log.log("Statistics "+key+" recalculated")
    Monitor.dispatch(new StatisticUpdatedEvent(this, Statistics.engine));
    Monitor.dispatch(new StatisticXUpdatedEvent(this, Statistics.engine));
  }
}

