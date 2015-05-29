package networkframework.core.engine

/**
 * 
 * 
 * DEPRECATED, bind to events instead
 * 
 * 
 * A class that holds termination conditions of the simulation. 
 */
abstract class Terminator() {
  /**
   * Convenient constructor
   */
  def this(e : SimulationEngine) = {this(); engine = Some(e)}
  
  /**
   * Having a simulation engine is optional
   */
  var engine : Option[SimulationEngine] = None
  
  /**
   * Actual stop condition
   */
  def stop: Boolean
}

case class StepTerminator(max : Integer, e: SimulationEngine) extends Terminator(e) {
  val limit = max
  
  /** 
   * Stop if a certain limit of steps is exceeded
   */
  override def stop = {engine match {
    case Some(engine : SimulationEngine) => engine.step > max
    case None => false
  }
  }
}

