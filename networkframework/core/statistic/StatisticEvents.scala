package networkframework.core.statistic

import networkframework.core.monitoring.Event
import networkframework.core.engine.SimulationEngine

case class StatisticUpdatedEvent(s: Statistic[_ <: Any], e : SimulationEngine) extends Event
{
  key = "statistics.statisticUpdated";
}

case class StatisticXUpdatedEvent(s: Statistic[_ <: Any], e : SimulationEngine) extends Event
{
  key = "statistics."+ s.key(0).toUpperCase + s.key.substring(1, s.key.length) +"Updated";
}