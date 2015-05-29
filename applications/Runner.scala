package applications
import applications.roads.RoadSimulation
import applications.virus._
import scala.collection.mutable.HashSet
import applications.social.SocialSimulation
import applications.socialvirus._

object Runner {
	var apps : List[Application] = Nil

	apps ::= new RoadSimulation
	apps ::= new TurnBasedVirusSimulation
	apps ::= new RoundBasedVirusSimulation
	apps ::= new EventBasedVirusSimulation
	apps ::= new SocialSimulation
	apps ::= new TBSocialVirusSimulation
	

	def printOptions = {
	  var i = 1;
	  for (app <- apps) yield {println("Application " + i + ": "+ app.name); i+=1}
	  
	}
	
	def describe(i : Integer) = if (i >= 1 & i <= apps.size) {println(apps(i-1).name + "\n"+ apps(i-1).description); true} else false;
	
	def start(i :Integer) = if (i >= 1 & i <= apps.size){ apps(i-1)init; apps(i-1).start; true} else false; 
}