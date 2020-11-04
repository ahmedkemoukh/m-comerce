package Agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;

import Container.ContainerBroker;
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

public class Vendeur extends Achteur {
	private ACLMessage resulta;
	



	private static String listA="listArticle";
	private static String listAR="listArticleRep";
	private static String listA1="listArticle1";
	private static String rech="RechArticle";
	private static String rechR="RechArticleRep";
	
	Fonction fon;
	private static final String Admin = "Admin";
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private String AgentPaiement="paiement";
	private String agentInter="";
	
	
	protected void setup() {
		resulta = new ACLMessage(ACLMessage.INFORM);
		fon=new Fonction();
		
super.setup();
	//addBehaviour(new Particip());



	
	
	addBehaviour(new Inscription(this));	
	addBehaviour(new ListVent(this));	

	 
	addBehaviour(new ListArticlV(this));
	addBehaviour(new RechArticleV(this));
	
	addBehaviour(new ArticlesAchatV(this));
	addBehaviour(new ArticlesAchatRepV(this));
	
	addBehaviour(new Addproduit(this));
	addBehaviour(new SuprimerProduit(this));
	
	addBehaviour(new ChangeEtat(this));

	}
	
	
	
/*	private class Particip extends Behaviour
	
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
		
	}*/
	
	
	private class ListArticlV extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(listAV);
		public ListArticlV(Agent a){super(a);}
		@Override
		public void action() {

			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.INFORM);
				agentInter=msg.getSender().getLocalName();
				resulta.clearAllReceiver();
				resulta.setConversationId(listAR);
				System.out.println("listArticle"+"vendeur");
				resulta.addReceiver(msg.getSender());
				try {
					
				
					
					System.err.println("******** listArticle vendeur");
					
				
					//System.err.println(fon.ListArticle().get(0));
					String s=msg.getContent().toString();
					System.err.println(s+"********");
					
			  
			  us.getMarkplace().setListArticle(new ArrayList<Article>(fon.ListArticle(s,us.getMarkplace().getId())));
				 us.getMarkplace().setType(s);
				
				
				if(!us.getMarkplace().getListArticle().isEmpty())
				{
					resulta.setContentObject(us.getMarkplace());
				//System.out.println("vendeur listMarkplace "+ us.getListMarkplace().size()+" "+us.getListMarkplace().iterator().next());
					send(resulta);
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("sddd");
					e.printStackTrace();
				}
				
			
			}
			else
            {
                block();
            }
		}
	}
	
	
	
	
	
	
	private class RechArticleV extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(rechV);
		public RechArticleV(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta = new ACLMessage(ACLMessage.PROPOSE);
				resulta.clearAllReceiver();
				
				try {
					ArticlRech Rech=(ArticlRech)msg.getContentObject();
					System.out.println(Rech.getMarque()+"   "+us.getMarkplace().getId());
					resulta.setConversationId(rechR);
					resulta.addReceiver(msg.getSender());
					
					
					 us.getMarkplace().setListArticle(new ArrayList<Article>(fon.recharcher(Rech,us.getMarkplace().getId())));
					 
					
					
					if(!us.getMarkplace().getListArticle().isEmpty())
					{
						System.out.println("recharcher Vendeur"+us.getMarkplace().getListArticle().size());
						resulta.setContentObject(us.getMarkplace());
						send(resulta);
					//System.out.println("vendeur listMarkplace "+ us.getListMarkplace().size()+" "+us.getListMarkplace().iterator().next());
					}
					
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					System.out.println("error");
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("sddd");
					e.printStackTrace();
				}
	
			}
			else
			{
				block();
			}
			
		}
		
	}
	
	private class ArticlesAchatV extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("achatArticle");
		public ArticlesAchatV(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.REQUEST);
				resulta.clearAllReceiver();
				
				try {
					Command vent=(Command)msg.getContentObject();
					
				//**	System.out.println(vent.getArticle().getNom_p()+"*****vendeur*****"+us.getNom_C());
					resulta.setConversationId("achatArticle");
					resulta.addReceiver(new AID(AgentPaiement,AID.ISLOCALNAME));
					vent.setVendeur(us);
					System.out.println("vendeur achter article"+vent.getLignsCommand().size());
					fon.updateqtArt(vent);
					//send(resulta);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					System.out.println("error");
					e.printStackTrace();
			
				
	
			
			} 
			}
			else
			{
				block();
			}
			
		}
		
	}
	
	private class ArticlesAchatRepV extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("achatArticleRepV");
		public ArticlesAchatRepV(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.CONFIRM);
				resulta.clearAllReceiver();
				
				try {
					Command vent=(Command)msg.getContentObject();
					if(!vent.getEtatpayment().equals("2"))
					{
						fon.updateqtArtR(vent);
					}
			
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					System.out.println("errorsssssssss");
					e.printStackTrace();
			
				
	
			} 
			}
			else
			{
				block();
			}
			
		}
		
	}
	
	
	private class Addproduit extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("addproduit");
		public Addproduit(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.CONFIRM);
				resulta.clearAllReceiver();
				
				try {
					Article a=(Article) msg.getContentObject();
					a.setMarkeplace(us.getMarkplace());
					
					for(byte[] b:a.getListImage())
					{
						String id=fon.addImage(b);
						if(!id.equals(""))
						{
							a.getImags_p().add(id);
						}
					}
					
					fon.saveProduit(a);
					
					resulta.addReceiver(msg.getSender());
					send(resulta);
				} catch (UnreadableException e) {
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
	
	
	private class Inscription extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("Inscription");
		public Inscription(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				
				try {
					Utilisateur us=(Utilisateur)msg.getContentObject();
					//**System.out.println(us.getEmail_C());
					resulta.setConversationId("repondAuth");
					resulta.addReceiver(msg.getSender());
					Utilisateur u=fon.insription(us);
				
					
					resulta.setContentObject(u);
					send(resulta);
					/*resulta.setContentObject((Serializable)fon.recharcher(Rech));
					System.out.println(fon.recharcher(Rech).size()+"*****");
					send(resulta);*/
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					System.out.println("error");
					e.printStackTrace();
				}  catch (IOException e) {
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
	
	private class SuprimerProduit extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("suparticle");
		public SuprimerProduit(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				
				try {
					Article a=(Article) msg.getContentObject();
					
					fon.suprimerArticle(a);
				} catch (UnreadableException e) {
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
	
	private class ListVent extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("listvent");
		public ListVent(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				
				try {
				System.out.println("ventAchat");
				resulta.addReceiver(msg.getSender());
				resulta.setConversationId("repondVent");
				
					resulta.setContentObject(fon.listVent(us));
					send(resulta);
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
	
	
	private class ChangeEtat extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("ChangeEtata");
		public ChangeEtat(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				
				try {
				System.out.println("ChangeEtata");
				resulta.addReceiver(msg.getSender());
				resulta.setConversationId("ChangeEtataR");
				String etat=(String)msg.getContent();
				fon.changeEtat(etat);
					resulta.setContent("success");
					send(resulta);
				} catch (Exception e) {
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
