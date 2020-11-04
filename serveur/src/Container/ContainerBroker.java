package Container;

import Agents.Achteur;
import Agents.Adminis;
import Agents.Paiement;
import Agents.Vendeur;
import fonction.Fonction;
import jade.core.ProfileImpl; import jade.core.Runtime;
import jade.wrapper.AgentContainer; import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
public class ContainerBroker {
	public static AgentContainer agentContainer;
	public static void main(String[] args) {
		try
		{
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl(false);//profilGui='false' Veut dire crée le conteneur 
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");//pour définir le port et adress ip de conteneur principal
		agentContainer=runtime.createAgentContainer(profileImpl);//crée le conteneur 
		AgentController Admin=agentContainer.createNewAgent("Admin",
		Adminis.class.getName(), new Object[]{}); //crée un agent  de nom 'admin' dans conteneur
		AgentController paiement=agentContainer.createNewAgent("paiement",
				Paiement.class.getName(), new Object[]{});
		
		Admin.start();//démarrer l'agent
		paiement.start();
		new Fonction().vendeur();
		agentvendeur();
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
	
	
	public static void createag(String s)
	{
		AgentController Admin = null;
		try {
			Admin = agentContainer.createNewAgent(s,
					Achteur.class.getName(), new Object[]{});
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
				
				try {
					Admin.start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public static void agentvendeur()
	{
		try
		{
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl(false);
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
		agentContainer=runtime.createAgentContainer(profileImpl);
		for(String a: MainContainer.listVendeur)
		{
		AgentController Admin=agentContainer.createNewAgent(a,
		Vendeur.class.getName(), new Object[]{});
		Admin.start();
		}
		
		
		

		} catch (Exception e) {
		e.printStackTrace();
		}
	}
}

