package applications.socialvirus

import applications.socialvirus.statistic._
import networkframework.core.engine.Module.Module
import networkframework.core.graph.attribute.Attribute
import networkframework.core.graph.Edge
import networkframework.core.graph.Node
import networkframework.modules.basestats.TotalRunningTime


/**
 * A sample application with a virus that can spread.
 * Actors can be vaccinated after which they can not get infected again
 * Actors can die when they are weak and infected
 * Actors can resist an infection
 * Actors can spread an infection depending on how likely their bond with another actor is
 * 
 * Eg a disease in a social network
 */
object VirusModule extends Module{
	
   /** Module name */
  key = "Virus";
	
   /** Dependencies */
  requirements += "Ubigraph";
  //requirements += "BaseStats";
  //requirements += "SocialNetwork";
	
  /** Statistics for this module */
  var piStat : PercentageInfected = new PercentageInfected();
  var pvStat : 	PercentageVaccinized = new PercentageVaccinized();
  var laStat : 	LivingActors = new LivingActors();
  var rtStat : 	TotalRunningTime = new TotalRunningTime();
  statistics += piStat
  statistics += pvStat
  statistics += laStat
  statistics += rtStat
	

  /** Attributes for this module */
  attributes += new Attribute[Node,String]("name",""); //whether person is infected or not
  attributes += new Attribute[Node,SocialGraph]("sn",null); //whether person is infected or not
  attributes += new Attribute[Node,Boolean]("infected",false); //whether person is infected or not
	attributes += new Attribute[Node,Double]("resistance",0.5);//resistance for the virus
	attributes += new Attribute[Node,Double]("spread",0.5);//chance of spreading a virus
	attributes += new Attribute[Edge,Double]("susceptibility",0.8);//chance the contact between 2 persons creates an infection
	attributes += new Attribute[Node,Double]("scared",0.5); //how scared a person is, the more scared, the more chance he will get a vaccin
	attributes += new Attribute[Node,Boolean]("vaccinated",false); //whether person is vaccinated
	attributes += new Attribute[Node,Double]("weakness",0.5); //Chance a person dies while infected
	attributes += new Attribute[Node,Double]("attractiveness", 0.5) // how attractive is this person
	
	
	/** Event handlers for this module */
	addHandler("statistics.LivingActorsUpdated",StopConditions.checkCondition);
	addHandler("statistics.PercentageVaccinizedUpdated",StopConditions.checkCondition);
	addHandler("statistics.RunningTimeUpdated",StopConditions.checkCondition);
	
	addHandler("attribute.VaccinatedUpdate",UbigraphBridge.checkNodeCondition);
	addHandler("attribute.InfectedUpdate",UbigraphBridge.checkNodeCondition);
	
	addHandler("statistics.LivingActorsUpdated",SocialBridge.checkCondition);

}
