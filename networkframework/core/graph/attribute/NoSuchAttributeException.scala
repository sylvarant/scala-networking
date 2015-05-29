package networkframework.core.graph.attribute

case class NoSuchAttributeException(val key:String)  extends Exception
{


	override def toString() : String = {
			"Attribute: ["+key+"] not found"
	}

}