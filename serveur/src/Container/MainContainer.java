package Container;
import java.util.ArrayList;
import java.util.List;

import jade.core.Profile; import jade.core.ProfileImpl;
import jade.core.Runtime; import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;

 public class MainContainer {
    public static List<String>listVendeur;
			public static void main(String[] args) {
			try{
				
			/*Runtime : Chaque application Java a une seule instance de classe Runtime 
			qui permet � l'application de s'interfacer avec l'environnement dans lequel l'application s'ex�cute.*/
			Runtime runtime=Runtime.instance();
			Properties properties=new ExtendedProperties();//pour d�finie la propri�t� de conteneur
			properties.setProperty(Profile.GUI,"true");//profilGui=' true' Veut dire cr�e le conteneur principal
			ProfileImpl profileImpl=new ProfileImpl(properties);
			AgentContainer mainContainer=runtime.createMainContainer(profileImpl); //cr�e le conteneur principal
			mainContainer.start(); //d�marrer le conteneur principal
			}
			catch(Exception e){ e.printStackTrace(); }
			}

	}

