/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
 *****************************************************************/

package com.allandroidprojects.ecomsample.agent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.support.annotation.RequiresApi;
import android.util.Log;

import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.ListArticlAchat;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Vente;
import com.allandroidprojects.ecomsample.miscellaneous.EmptyActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;


import chat.ontology.ChatOntology;

import jade.content.ContentManager;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import jade.util.leap.Iterator;
import jade.util.leap.Set;
import jade.util.leap.SortedSetImpl;

/**
 * This agent implements the logic of the chat client running on the user
 * terminal. User interactions are handled by the ChatGui in a
 * terminal-dependent way. The ChatClientAgent performs 3 types of behaviours: -
 * ParticipantsManager. A CyclicBehaviour that keeps the list of participants up
 * to date on the basis of the information received from the ChatManagerAgent.
 * This behaviour is also in charge of subscribing as a participant to the
 * ChatManagerAgent. - ChatListener. A CyclicBehaviour that handles messages
 * from other chat participants. - ChatSpeaker. A OneShotBehaviour that sends a
 * message conveying a sentence written by the user to other chat participants.
 * 
 * @author Giovanni Caire - TILAB
 */
public class ChatClientAgent extends Agent implements ChatClientInterface {
	private static final long serialVersionUID = 1594371294421614291L;

	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	private static final String CHAT_ID = "__chat__";
	private static final String CHAT_MANAGER_NAME = "Admin";

	private Set participants = new SortedSetImpl();
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private ACLMessage spokenMsg;
	public static  Utilisateur monAchteur;


	private static final String Admin = "Admin";
	private Context context;

	protected void setup() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			if (args[0] instanceof Context) {
				context = (Context) args[0];
			}
		}
		
		// Register language and ontology
		ContentManager cm = getContentManager();
		cm.registerLanguage(codec);
		cm.registerOntology(onto);
		cm.setValidationMode(false);

		// Add initial behaviours
		addBehaviour(new ParticipantsManager(this));
		addBehaviour(new ChatListener(this));

		// Initialize the message used to convey spoken sentences
		spokenMsg = new ACLMessage(ACLMessage.REQUEST);


		// Activate the GUI
		registerO2AInterface(ChatClientInterface.class, this);

		addBehaviour(new RepondRech(this));
		addBehaviour(new RepondListArticle(this));
		addBehaviour(new ArticlesAchatRep(this));
		addBehaviour(new ListVentRep(this));
		addBehaviour(new ListAchatRep(this));
		addBehaviour(new ChangeEtatR(this));
	}

	protected void takeDown() {
	}

	private void notifyParticipantsChanged() {
		Intent broadcast = new Intent();
		broadcast.setAction("jade.demo.chat.REFRESH_PARTICIPANTS");
		logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction());
		context.sendBroadcast(broadcast);
	}

	private void notifySpoken(String speaker, String sentence) {
		Intent broadcast = new Intent();
		broadcast.setAction("jade.demo.chat.REFRESH_CHAT");
		broadcast.putExtra("sentence", speaker + ": " + sentence + "\n");
		logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction());
		context.sendBroadcast(broadcast);
	}
	
	/**
	 * Inner class ParticipantsManager. This behaviour registers as a chat
	 * participant and keeps the list of participants up to date by managing the
	 * information received from the ChatManager agent.
	 */
	class ParticipantsManager extends CyclicBehaviour {
		private static final long serialVersionUID = -4845730529175649756L;
		private MessageTemplate template;

		ParticipantsManager(Agent a) {
			super(a);
		}

		public void onStart() {
			// Subscribe as a chat participant to the ChatManager agent
			ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
			subscription.setLanguage(codec.getName());
			subscription.setOntology(onto.getName());
			String convId = "C-" + myAgent.getLocalName();
			subscription.setConversationId(convId);
			subscription
					.addReceiver(new AID(CHAT_MANAGER_NAME, AID.ISLOCALNAME));
			myAgent.send(subscription);
			// Initialize the template used to receive notifications
			// from the ChatManagerAgent
			template = MessageTemplate.MatchConversationId("repondAuth");
		}

		public void action() {
			// Receives information about people joining and leaving
			// the chat from the ChatManager agent
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM) {
					try {
						monAchteur=(Utilisateur) msg.getContentObject();
						logger.log(Logger.INFO, "repondAUT***************************: " + monAchteur);
						Intent broadcast = new Intent();
                    if(monAchteur!=null) {


						broadcast.setAction("CreateAgent");
						logger.log(Level.INFO, "CreateAgent " + broadcast.getAction());

					}
					else
					{
						broadcast.setAction("invalid");
					}
						context.sendBroadcast(broadcast);
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} else {
					handleUnexpected(msg);
				}
			} else {
				block();
			}
		}
	} // END of inner class ParticipantsManager

	/**
	 * Inner class ChatListener. This behaviour registers as a chat participant
	 * and keeps the list of participants up to date by managing the information
	 * received from the ChatManager agent.
	 */
	class ChatListener extends CyclicBehaviour {
		private static final long serialVersionUID = 741233963737842521L;
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(CHAT_ID);

		ChatListener(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM) {
					notifySpoken(msg.getSender().getLocalName(),
							msg.getContent());
				} else {
					handleUnexpected(msg);
				}
			} else {
				block();
			}
		}
	} // END of inner class ChatListener

	/**
	 * Inner class ChatSpeaker. INFORMs other participants about a spoken
	 * sentence
	 */
	private class ChatSpeaker extends OneShotBehaviour {
		private static final long serialVersionUID = -1426033904935339194L;
		private String sentence;

		private ChatSpeaker(Agent a, String s) {
			super(a);
			sentence = s;
		}

		public void action() {
			spokenMsg.clearAllReceiver();
			Iterator it = participants.iterator();
			while (it.hasNext()) {
				spokenMsg.addReceiver((AID) it.next());
			}
			spokenMsg.setContent(sentence);
			notifySpoken(myAgent.getLocalName(), sentence);
			send(spokenMsg);
		}
	} // END of inner class ChatSpeaker



	private class ListArticle extends OneShotBehaviour
	{
		private String s;
		private int curentype;
      public ListArticle(Agent e,String s,int curentype)
	  {
	  	super(e);
	  	this.s=s;
	  	this.curentype=curentype;
	  }
		@Override
		public void action() {
			spokenMsg = new ACLMessage(ACLMessage.REQUEST);
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("listArticle");
			if(curentype==1)
				spokenMsg.setConversationId("listArticleV");
			logger.log(Logger.INFO, "Content is*********envo******************: "+curentype+" "+monAchteur);
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));
			spokenMsg.setContent(s);
			send(spokenMsg);
		}
	}


	private class RechercheArticle extends OneShotBehaviour
	{
		private ArticlRech s;
		public RechercheArticle(Agent e,ArticlRech s)
		{
			super(e);
			this.s=s;
		}
		@Override
		public void action() {
			spokenMsg = new ACLMessage(ACLMessage.REQUEST);
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("RechArticle");
			logger.log(Logger.INFO, "Content is*********rech******************: " +monAchteur);
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(s);
			} catch (IOException e) {
				logger.log(Logger.INFO, "error*********rech******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}
	private class RepondListArticle extends CyclicBehaviour {
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("listArticleRep");

		RepondListArticle(Agent a) {
			super(a);
		}

		@RequiresApi(api = Build.VERSION_CODES.N)
		@Override
		public void action() {
			ACLMessage msg =receive(template);

			if (msg != null) {


					try {
                  Markeplace A=(Markeplace) msg.getContentObject();
						Intent broadcast = new Intent();
						broadcast.setAction("ListArticle");
						broadcast.putExtra("ListArt", (Serializable) A);
						logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction()+"  ");

						logger.log(Logger.INFO, "************************ggggggg****************************  "
							);
						context.sendBroadcast(broadcast);
					} catch (UnreadableException e) {
						logger.log(Logger.INFO, "************************ggggggg1**************************** ");
						e.printStackTrace();
					}


			}
			else
			{block();}
		}
	}

	private class RepondRech extends CyclicBehaviour {
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("repondRecharch");

		RepondRech(Agent a) {
			super(a);
		}

		@RequiresApi(api = Build.VERSION_CODES.N)
		@Override
		public void action() {
			ACLMessage msg =receive(template);

			if (msg != null) {

					try {
						Markeplace a=(Markeplace) msg.getContentObject();
						logger.log(Logger.INFO, "************************gggggggrechr**************************** "
								+a.getListArticle().size());
//                        logger.log(Level.INFO, "brod recharch **************************"+a.get(0).getMarque_p());
						Intent broadcast = new Intent();
						broadcast.setAction("repondRecherch");
						broadcast.putExtra("repondRecherch", (Serializable) a);
						context.sendBroadcast(broadcast);
					} catch (UnreadableException e) {
						logger.log(Logger.INFO, "************************ggggggg1rechr**************************** ");
						e.printStackTrace();


				}

			}
			else
			{block();}
		}
	}

	// ///////////////////////////////////////
	// Methods called by the interface
	// ///////////////////////////////////////
	public void handleSpoken(String s) {
		// Add a ChatSpeaker behaviour that INFORMs all participants about
		// the spoken sentence
		addBehaviour(new ChatSpeaker(this, s));
	}

	public void DemandArticle (String s,int curentype)
	{
		logger.log(Logger.INFO, "Content is: **************de*******************");
		addBehaviour(new ListArticle(this,s,curentype));
	}

	public void recherchArticle (ArticlRech a)
	{
		logger.log(Logger.INFO, "Content is: **************de*******************");
		addBehaviour(new RechercheArticle(this,a));
	}

	@Override
	public void AchatArt(Command a) {
		logger.log(Logger.INFO, "achat: **************de*******************");
		addBehaviour(new ArticlesAchat(this,a));
	}

	@Override
	public void autentifier(Utilisateur u) {
		logger.log(Logger.INFO, "auth: **************de*******************");
		addBehaviour(new Autentifier(this,u));
	}

	@Override
	public void inscription(Utilisateur u) {
		logger.log(Logger.INFO, "inscription: **************de*******************");
		monAchteur=null;
		addBehaviour(new Inscription(this,u));
	}

	@Override
	public void addproduit(Article a) {
		logger.log(Logger.INFO,"add produit");
		addBehaviour(new Addproduit(this,a));
	}

	@Override
	public void suparticle(Article a) {
		logger.log(Logger.INFO,"suprimer produit");
		addBehaviour(new SupArticle(this,a));
	}

	@Override
	public void listVend() {
		logger.log(Logger.INFO,"Liste achat");
		addBehaviour(new ListVent(this));
	}

	@Override
	public void listAchat() {
		logger.log(Logger.INFO,"Liste achat");
		addBehaviour(new ListAchat(this));
	}

	@Override
	public void addMarkPlace(Markeplace getMarkplace) {
		addBehaviour(new AddMarkPlace(this,getMarkplace));
	}

	@Override
	public void changeEtat(String a) {
		addBehaviour(new ChangeEtate(this,a));
	}


	private class ChangeEtate extends OneShotBehaviour
	{
		String etat;
      public ChangeEtate(Agent a,String e)
	  {
	  	super(a);
	  	etat=e;

	  }
		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("ChangeEtata");
			logger.log(Logger.INFO, "Content is*********achat******************: " +monAchteur);
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U
					()+" "+monAchteur.getPrenom_U
					(),AID.ISLOCALNAME));
			try {
				spokenMsg.setContent(etat);
			} catch (Exception e) {
				logger.log(Logger.INFO, "error*********achat******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}

	public String[] getParticipantNames() {
		String[] pp = new String[participants.size()];
		Iterator it = participants.iterator();
		int i = 0;
		while (it.hasNext()) {
			AID id = (AID) it.next();
			pp[i++] = id.getLocalName();
		}
		return pp;
	}

	// ///////////////////////////////////////
	// Private utility method
	// ///////////////////////////////////////
	private void handleUnexpected(ACLMessage msg) {
		if (logger.isLoggable(Logger.WARNING)) {
			logger.log(Logger.WARNING, "Unexpected message received from "
					+ msg.getSender().getName());
			logger.log(Logger.WARNING, "Content is: " + msg.getContent());
		}
	}


	private class ArticlesAchat extends OneShotBehaviour {
        Command s;
		public ArticlesAchat(Agent e,Command s)
		{
			super(e);
			this.s=s;
		}
		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("achatArticles");
			logger.log(Logger.INFO, "Content is*********achat******************: " +monAchteur);
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U
					()+" "+monAchteur.getPrenom_U
					(),AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(s);
			} catch (IOException e) {
				logger.log(Logger.INFO, "error*********achat******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}


	private class Autentifier extends OneShotBehaviour {
		Utilisateur s;
		public Autentifier(Agent e,Utilisateur s)
		{
			super(e);
			this.s=s;
		}
		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("autentifier");
			logger.log(Logger.INFO, "Content is*********autentifier******************: " +Admin);
			spokenMsg.addReceiver(new AID(Admin,AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(s);
			} catch (IOException e) {
				logger.log(Logger.INFO, "error*********authentifier******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}


	private class AddMarkPlace extends OneShotBehaviour {
		Markeplace s;
		public AddMarkPlace(Agent e,Markeplace s)
		{
			super(e);
			this.s=s;
		}
		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("addmarkplace");
			logger.log(Logger.INFO, "Content is*********addmarkplace******************: " +Admin);
			spokenMsg.addReceiver(new AID(Admin,AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(s);
			} catch (IOException e) {
				logger.log(Logger.INFO, "error*********addmarkplace******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}
	private class Inscription extends OneShotBehaviour {
		Utilisateur s;
		public Inscription(Agent e,Utilisateur s)
		{
			super(e);
			this.s=s;
		}
		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("Inscription");
			logger.log(Logger.INFO, "Content is*********inscription******************: " +Admin);

				spokenMsg.addReceiver(new AID(Admin,AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(s);
			} catch (IOException e) {
				logger.log(Logger.INFO, "error*********inscr******************: " +monAchteur);
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}

	private class Addproduit extends OneShotBehaviour {
		private Article a;
		public Addproduit(Agent e,Article a) {
			super(e);
			this.a=a;
		}

		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("addproduit");
			logger.log(Logger.INFO, "Content is*********addproduit******************: " +monAchteur+" "+a.getListImage().size());
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(a);
			} catch (IOException e) {
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}

	private class SupArticle extends OneShotBehaviour {
		private Article a;
		public SupArticle(Agent e,Article a) {
			super(e);
			this.a=a;
		}

		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("suparticle");
			logger.log(Logger.INFO, "Content is*********suparticle******************: " +monAchteur+" ");
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));
			try {
				spokenMsg.setContentObject(a);
			} catch (IOException e) {
				e.printStackTrace();
			}
			send(spokenMsg);
		}
	}

	private class ListVent extends OneShotBehaviour {

		public ListVent(Agent e) {
			super(e);

		}

		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("listvent");
			logger.log(Logger.INFO, "Content is*********ListVent******************: " +monAchteur+" ");
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));

			send(spokenMsg);
		}
	}

	private class ListAchat extends OneShotBehaviour {

		public ListAchat(Agent e) {
			super(e);

		}

		@Override
		public void action() {
			spokenMsg.clearAllReceiver();
			spokenMsg.setConversationId("listAchat");
			logger.log(Logger.INFO, "Content is*********ListVent******************: " +monAchteur+" ");
			spokenMsg.addReceiver(new AID(monAchteur.getNom_U()+" "+monAchteur.getPrenom_U(),AID.ISLOCALNAME));

			send(spokenMsg);
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

   ImageUrlUtils list=new ImageUrlUtils();
				try {
					ArrayList<Command> vent=(ArrayList<Command>) msg.getContentObject();
                 /*   if(vent.getEtat()==1)
					{
					/*for(Command a:list.getCartListImageUri())
					{
						if(a.getArticle().getId_P()==vent.getArticle().getId_P())
                            list.getCartListImageUri().remove(a);
					}
					}*/
                 if(vent.get(0).getEtatpayment().equals("2")) {
					 ImageUrlUtils.listArt.addAll(vent);
				 }
					Log.d("info","********vent succes*******"+vent.toString());
					Intent broadcast = new Intent();
					broadcast.setAction("resultaPaymrnt");
					broadcast.putExtra("resultaPaymrnt", (Serializable) vent.get(0).getEtatpayment());
					context.sendBroadcast(broadcast);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}


			}
			else
			{
				block();
			}

		}

	}

	private class ListVentRep extends CyclicBehaviour
	{

		private MessageTemplate template = MessageTemplate
				.MatchConversationId("repondVent");
		public ListVentRep(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {


				try {
					Utilisateur vent=(Utilisateur) msg.getContentObject();

					Log.d("info","*******Vent****succes****");
					Intent broadcast = new Intent();
					broadcast.setAction("List_Vend");
					broadcast.putExtra("List_Vend", vent);
					context.sendBroadcast(broadcast);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}


			}
			else
			{
				block();
			}

		}

	}

	private class ListAchatRep extends CyclicBehaviour
	{

		private MessageTemplate template = MessageTemplate
				.MatchConversationId("listachatrep");
		public ListAchatRep(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {


				try {
					Utilisateur vent=(Utilisateur) msg.getContentObject();

					Log.d("info","*******achat********");
					Intent broadcast = new Intent();
					broadcast.setAction("List_Achat");
					broadcast.putExtra("List_Achat", vent);
					context.sendBroadcast(broadcast);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}


			}
			else
			{
				block();
			}

		}

	}



	private class ChangeEtatR extends CyclicBehaviour
	{

		private MessageTemplate template = MessageTemplate
				.MatchConversationId("ChangeEtataR");
		public ChangeEtatR(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {


				try {


					Log.d("info","*******ChangeEtat********");
					Intent broadcast = new Intent();
					broadcast.setAction("ChangeEtat");
					broadcast.putExtra("ChangeEtat","livre");
					context.sendBroadcast(broadcast);
				} catch (Exception e) {
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
