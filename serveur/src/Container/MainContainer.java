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
			qui permet à l'application de s'interfacer avec l'environnement dans lequel l'application s'exécute.*/
			Runtime runtime=Runtime.instance();
			Properties properties=new ExtendedProperties();//pour définie la propriété de conteneur
			properties.setProperty(Profile.GUI,"true");//profilGui=' true' Veut dire crée le conteneur principal
			ProfileImpl profileImpl=new ProfileImpl(properties);
			AgentContainer mainContainer=runtime.createMainContainer(profileImpl); //crée le conteneur principal
			mainContainer.start(); //démarrer le conteneur principal
			}
			catch(Exception e){ e.printStackTrace(); }
			}

	}

