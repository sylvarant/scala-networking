package applications
import applications.virus._
import applications.socialvirus.TBSocialVirusSimulation
object Test {
	

	
	def main(args: Array[String]) {		
		
		var sim : Application = new TBSocialVirusSimulation;
		sim.init;
		sim.start;
		
	}
	

}