package networkframework.core.graph

object UniqueId{
    
  private var  _counter : Int = -1
  
  def newid() : Int = {_counter += 1; _counter}

}