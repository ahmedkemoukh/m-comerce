package Agents;


import java.io.IOException;
import java.util.ArrayList;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.ListArticlAchat;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;

import chat.ontology.ChatOntology;
import fonction.Fonction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


public class Paiement  extends Agent {
	private ACLMessage resulta;
	private static String listA="listArticle";
	private static String listA1="listArticle1";
	private static String rech="RechArticle";
	Fonction fon;
	private static final String Admin = "Admin";
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private String AgentPaiement="paiement";
	
	
	
	protected void setup() {
		resulta = new ACLMessage(ACLMessage.CONFIRM);
		fon=new Fonction();
	//addBehaviour(new Particip());
		addBehaviour(new ArticlesAchat(this));
		addBehaviour(new ArticlConf(this));

	}
	
	
	
private class Particip extends Behaviour
	
	{

		@Override
		public void action() {
			System.out.println("achteur");
			ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
			subscription.setLanguage(codec.getName());
			subscription.setOntology(onto.getName());
			String convId = "C-" + myAgent.getLocalName();
			subscription.setConversationId(convId);
			subscription
					.addReceiver(new AID(Admin, AID.ISLOCALNAME));
			myAgent.send(subscription);
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return true;
		}
		
	}

private class ArticlConf extends CyclicBehaviour
{
	private MessageTemplate template = MessageTemplate
			.MatchConversationId("confirm");
	public ArticlConf(Agent a) {
		super(a);
	}
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(template);
		if (msg != null) {
		ACLMessage resulta= new ACLMessage(ACLMessage.CONFIRM);
		resulta.setConversationId("achatArticlesP");
		try {
		Command achatArt=(Command)msg.getContentObject();
		resulta.clearAllReceiver();
		resulta.addReceiver(msg.getSender());
		resulta.setContentObject(achatArt);
		send(resulta);
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else
		{
			block();
		}
		
	}
	
}

private class ArticlesAchat extends CyclicBehaviour
{
	
	private MessageTemplate template = MessageTemplate
			.MatchConversationId("achatArticle");
	public ArticlesAchat(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(template);
		if (msg != null) {
			resulta.clearAllReceiver();
			resulta.setConversationId("achatArticleRepV");
			try {
				ArrayList<Command> vent=(ArrayList<Command>)msg.getContentObject();
				
	//**			System.err.println(vent.getArticle().toString()+" "+vent.getArticle().getUser().toString()+" "+vent.getUser().toString());
			    //vent=fon.paiment(vent);
			System.err.println("paimentsss *****"+msg.getSender().getLocalName());
			String etat=fon.payment(vent);
             System.out.println("*******************"+etat);
             
			for(Command a:vent)
			{
				resulta.clearAllReceiver();
				String key=a.getVendeur().getNom_U()+" "+a.getVendeur().getPrenom_U();
				System.err.println("paiment *****"+key);
				resulta.addReceiver(new AID(key,AID.ISLOCALNAME));
				
				a.setEtatpayment(etat);
				if(a.getEtatpayment().equals("2"))
					fon.saveVent(a);
				resulta.setContentObject(a);
				send(resulta);
			}
			resulta.setConversationId("achatArticleRep");
			resulta.clearAllReceiver();
			resulta.addReceiver(msg.getSender());
			vent.get(0).setEtatpayment(etat);
			resulta.setContentObject(vent);
			send(resulta);
			
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				System.out.println("error");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		else
		{
			block();
		}
		
	}
	
}



	
}
