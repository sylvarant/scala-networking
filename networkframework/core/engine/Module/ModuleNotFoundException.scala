package networkframework.core.engine.Module

case class ModuleNotFoundException(val key:String)  extends Exception
{


	override def toString() : String = {
			"Required Module: ["+key+"] not found"
	}

}