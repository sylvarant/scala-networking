package networkframework.core.engine


/**
 * Call when simulation starts
 */
case class SimulationStartEvent(e: SimulationEngine) extends networkframework.core.monitoring.Event
{
  key = "engine.simulationStart"
}

/**
 * Call when simulation update round has finished
 */
case class SimulationUpdateEvent(e: SimulationEngine) extends networkframework.core.monitoring.Event
{
  key = "engine.update"
}

/**
 * Call when simulation is done
 */
case class SimulationDoneEvent(e: SimulationEngine) extends networkframework.core.monitoring.Event
{
  key = "engine.simulationDone"
}
