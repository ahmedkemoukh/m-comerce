package Agents;









import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.allandroidprojects.ecomsample.Mcommerce.Art;
import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.ListArticlAchat;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;

import BD.ConfigBD;
import Container.MainContainer;
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

public class Achteur extends Agent {
	private ACLMessage resulta;
	public Utilisateur us;
	public Utilisateur getUs() {
		return us;
	}



	public void setUs(Utilisateur us) {
		this.us = us;
	}



	private static String listA="listArticle";
	private static String listA1="listArticle1";
	private static String rech="RechArticle";
	private static String rechR="RechArticleRep";
	private static String listAR="listArticleRep";
	static String listAV="listArticleV";
	 static String rechV="RechArticleV";
	Fonction fon;
	private static final String Admin = "Admin";
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private String AgentPaiement="paiement";
	private String agentInter="";
	
	
	protected void setup() {
		resulta = new ACLMessage(ACLMessage.INFORM);
		fon=new Fonction();
		this.setUs(fon.getUtilisateur(this.getLocalName()));
	

	addBehaviour(new Particip());
	addBehaviour(new ListArticl(this));
	addBehaviour(new RechArticle(this));
	
	addBehaviour(new ArticlesAchat(this));

	addBehaviour(new ListArticlRep(this));
	addBehaviour(new RechArticleRep(this));
	addBehaviour(new ArticlesAchatRep(this));
	addBehaviour(new ListAchat(this));
	addBehaviour(new AticlePaiment(this));
	
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
	

	private class ListArticl extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate.MatchConversationId(listA);
		
		public ListArticl(Agent a){super(a);}
		@Override
		public void action() {
			
			ACLMessage msg = myAgent.receive(template);
			
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.REQUEST);
				
				agentInter=msg.getSender().getLocalName();
				
				resulta.clearAllReceiver();
				
				resulta.setConversationId(listAV);
				
				System.out.println("listArticle");
				
				for(String A : MainContainer.listVendeur)
				{
					if(!A.equals(getLocalName()))
						
					resulta.addReceiver(new AID(A,AID.ISLOCALNAME));	
				}
				try {

					String s=msg.getContent().toString();
					
					System.err.println(s+"******** listArticle");
					
					resulta.setContent(s);
					
					send(resulta);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("eroor achteur listArticle ");
					e.printStackTrace();
				}
			
			}
			else
            {
                block();
            }
		}
	}
	
	private class ListArticlRep extends CyclicBehaviour
	{
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(listAR);
		
		ListArticlRep(Agent a) {
			super(a);
		}

	
		public void action() {
			ACLMessage msg =receive(template);
		
			if (msg != null) {
				ACLMessage resulta = new ACLMessage(ACLMessage.INFORM);
				resulta.clearAllReceiver();
				resulta.setConversationId(listAR);
				resulta.addReceiver(new AID(agentInter,AID.ISLOCALNAME));
                  try {
					Markeplace u=(Markeplace) msg.getContentObject();
					System.err.println("******** listArticleRepond"+msg.getSender().getLocalName());
					System.err.println(u);
					resulta.setContentObject(u);
					
					send(resulta);
					
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
						
				}
				else
				{block();}
		
	}
	
	}
	
	
	
	
	
	
	
	
	private class RechArticle extends CyclicBehaviour
	{
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(rech);
		public RechArticle(Agent a) {
			super(a);
		}
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);//pour reception message 
			if (msg != null) {
				resulta.clearAllReceiver();			
				try {
					ArticlRech Rech=(ArticlRech)msg.getContentObject();
					System.out.println(Rech.getMarque());
					
					resulta.setConversationId(rechV);//defini id de convarsation
					
					for(String A : MainContainer.listVendeur)
					{
						if(!A.equals(getLocalName()))
						{
						
						resulta.addReceiver(new AID(A,AID.ISLOCALNAME));//créer boîte de envoyer
						}
					}
					
					resulta.setContentObject(Rech);//ajouter contenu de message
					System.out.println("rechercher*****");
					
					send(resulta);//envoyer message
					
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
	
	
	private class RechArticleRep extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(rechR);
		public RechArticleRep(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				
				try {
					Markeplace u=(Markeplace) msg.getContentObject();
					//System.out.println(Rech.getMarque());
					resulta.setConversationId("repondRecharch");
					resulta.addReceiver(new AID(agentInter,AID.ISLOCALNAME));
					
					
					resulta.setContentObject(u);
					System.out.println("rechercher*****"+msg.getSender().getLocalName());
					send(resulta);
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
	
	private class AticlePaiment extends CyclicBehaviour
	{
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("achatArticlessss");
		public AticlePaiment(Agent a) {
			super(a);
		}
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
			ACLMessage resulta= new ACLMessage(ACLMessage.REQUEST);
			resulta.setConversationId("confirm");
			try {
			Command achatArt=(Command)msg.getContentObject();
			resulta.clearAllReceiver();
			resulta.addReceiver(new AID(AgentPaiement,AID.ISLOCALNAME));
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
				.MatchConversationId("achatArticles");
		public ArticlesAchat(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				ACLMessage resulta= new ACLMessage(ACLMessage.REQUEST);
				resulta.setConversationId("achatArticle");
				
				try {
					
					Command achatArt=(Command)msg.getContentObject();
					System.out.println("******************");
				//**	us.setCodepostal_C(achatArt.getCodPost());
					 HashMap<String,ArrayList<LingeCommand>> hash_map = new HashMap<String,ArrayList<LingeCommand>>(); 
					for(LingeCommand a:achatArt.getLignsCommand())
					{
						String key=a.getArticle().getMarkeplace().getUser().getNom_U()+" "+a.getArticle().getMarkeplace().getUser().getPrenom_U();

						if(hash_map.containsKey(key))
						{
							hash_map.get(key).add(a);
						}
						else
						{
							hash_map.put(key,new ArrayList<LingeCommand>());
							hash_map.get(key).add(a);
						}
					}
					
					ArrayList<Command>listVent=new ArrayList<Command>();
					for(Map.Entry<String,ArrayList<LingeCommand>> entry : hash_map.entrySet()) {
					    String key = entry.getKey();
					    ArrayList<LingeCommand> value = entry.getValue();
					    Command achatArt1=new Command();
					    achatArt1.setLignsCommand(value);
					    achatArt1.setAchteur(achatArt.getAchteur());
					    achatArt1.setVendeur(value.get(0).getArticle().getMarkeplace().getUser());
					    System.out.println(achatArt1.getVendeur().getPrenom_U()+"*********************");
					  //**		vent.setUser(us);
						//	vent.setArticle(a);
							resulta.clearAllReceiver();
						resulta.addReceiver(new AID(key,AID.ISLOCALNAME));
							resulta.setContentObject(achatArt1);
							listVent.add(achatArt1);
							System.out.println("achteur achter article"+value.size());
							send(resulta);
					    // do what you have to do here
					    // In your case, another loop.
					}
					resulta.clearAllReceiver();
				
					resulta.addReceiver(new AID(AgentPaiement,AID.ISLOCALNAME));
					resulta.setContentObject((Serializable)listVent);
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
	

	private class ArticlesAchatRep extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("achatArticleRep");
		public ArticlesAchatRep(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				resulta.clearAllReceiver();
				ACLMessage resulta= new ACLMessage(ACLMessage.CONFIRM);
				try {
					ArrayList<Command> vent=(ArrayList<Command>)msg.getContentObject();
					
					
					resulta.setConversationId("achatArticleRep");
					resulta.addReceiver(new AID(agentInter,AID.ISLOCALNAME));
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
	
	
	
	private class ListAchat extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("listAchat");
		public ListAchat(Agent a) {
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
				resulta.setConversationId("listachatrep");
				
					resulta.setContentObject(fon.listAchat(us));
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
	
	
	
	
}
