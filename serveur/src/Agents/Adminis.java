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

package Agents;

//#J2ME_EXCLUDE_FILE

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.BasicOntology;
import jade.content.abs.*;

import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.proto.SubscriptionResponder.Subscription;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.FailureException;

import jade.domain.introspection.IntrospectionOntology;
import jade.domain.introspection.Event;
import jade.domain.introspection.DeadAgent;
import jade.domain.introspection.AMSSubscriber;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;

import Container.ContainerBroker;
import GuiAdm.CV;
import GuiAdm.Home17;
import GuiAdm.tabel;

import java.util.HashMap;
import java.util.Iterator;

import chat.client.agent.ChatClientAgent;
import chat.ontology.*;
import fonction.Fonction;

/**
   This agent maintains knowledge of agents currently attending the 
   chat and inform them when someone joins/leaves the chat.
   @author Giovanni Caire - TILAB
 */
public class Adminis extends Agent implements SubscriptionManager {
	public Map<AID, Subscription> participants = new HashMap<AID, Subscription>();
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private AMSSubscriber myAMSSubscriber;
	private ACLMessage spokenMsg;
	private Home17 home;
	
	public static  tabel listVendeur;
	public static  CV cvVendeur;
	Fonction fon;
	int i=0;
	AgentContainer agentContainer;
	protected void setup() {
		
		
		fon=new Fonction();
		
		
		home=new Home17(this);
		home.setVisible(true);
		listVendeur=new tabel();
		cvVendeur=new CV();
		
		
		// Prepare to accept subscriptions from chat participants
	
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);

		MessageTemplate sTemplate = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),
				MessageTemplate.and(
						MessageTemplate.MatchLanguage(codec.getName()),
						MessageTemplate.MatchOntology(onto.getName()) ) );
		addBehaviour(new SubscriptionResponder(this, sTemplate, this));
		spokenMsg = new ACLMessage(ACLMessage.INFORM);
		
		// Register to the AMS to detect when chat participants suddenly die
		myAMSSubscriber = new AMSSubscriber() {
			protected void installHandlers(Map handlersTable) {
				// Fill the event handler table. We are only interested in the
				// DEADAGENT event
				handlersTable.put(IntrospectionOntology.DEADAGENT, new EventHandler() {
					public void handle(Event ev) {
						DeadAgent da = (DeadAgent)ev;
						AID id = da.getAgent();
						// If the agent was attending the chat --> notify all
						// other participants that it has just left.
						if (participants.containsKey(id)) {
							try {
								deregister((Subscription) participants.get(id));
							}
							catch (Exception e) {
								//Should never happen
								e.printStackTrace();
							}
						}
					}
				});
			}
		};
		addBehaviour(myAMSSubscriber);
		addBehaviour(new Autentifier(this));
		addBehaviour(new Inscription(this));
		addBehaviour(new AddMarkPlace(this));
		
	}

	protected void takeDown() {
		// Unsubscribe from the AMS
		send(myAMSSubscriber.getCancel());
		//FIXME: should inform current participants if any
	}

	///////////////////////////////////////////////
	// SubscriptionManager interface implementation
	///////////////////////////////////////////////
	public boolean register(Subscription s) throws RefuseException, NotUnderstoodException { 
		try {
			AID send = s.getMessage().getSender();
			
			System.out.println("***************"+send.getName());
			
			participants.put(send, s);
			
			
			
			
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RefuseException("Subscription error");
		}		
	}

	public boolean deregister(Subscription s) throws FailureException {
		AID oldId = s.getMessage().getSender();
		// Remove the subscription
		participants.remove(oldId);
		System.out.println("remove"+oldId.getName());
		return false;
	}
	
	
	private class ListArticl extends CyclicBehaviour
	{
		int i=0;
		AgentContainer agentContainer;
		private MessageTemplate temp = MessageTemplate
				.MatchConversationId("ajouter");
		public ListArticl(Agent a){super(a);
		Runtime runtime=Runtime.instance();
		ProfileImpl profileImpl=new ProfileImpl(false);
		profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
		agentContainer=runtime.createAgentContainer(profileImpl);
		}
		@Override
		public void action() {

			ACLMessage msg = myAgent.receive(temp);
			if (msg != null) {
			
				try
				{
				System.out.println(participants);
				AgentController Admin=agentContainer.createNewAgent("Achteur"+(i++),
				Achteur.class.getName(), new Object[]{});
				
				
				Admin.start();
				
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
	
	
	private class Autentifier extends CyclicBehaviour
	{
		
		private MessageTemplate template = MessageTemplate
				.MatchConversationId("autentifier");
		public Autentifier(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				spokenMsg.clearAllReceiver();
				
				try {
					Utilisateur us=(Utilisateur)msg.getContentObject();
					System.out.println(us.getEmail_U());
					spokenMsg.setConversationId("repondAuth");
					spokenMsg.addReceiver(msg.getSender());
					Utilisateur u=fon.authentifier(us);
					if(u!=null)
					{
						AID send=msg.getSender();
						spokenMsg.clearAllReceiver();
						String convId = "C-" + send.getLocalName();
						
						
						spokenMsg.addReceiver(send);
						System.out.println("ssssssss"+" "+convId+" "+send.getHap());
						i++;
					
						Runtime runtime=Runtime.instance();
						ProfileImpl profileImpl=new ProfileImpl(false);
						profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
						//agentContainer=runtime.createAgentContainer(profileImpl);
						if(u.getType()!=1)
						ContainerBroker.createag(u.getNom_U()+" "+u.getPrenom_U());
							
						
					}
					
					spokenMsg.setContentObject(u);
					send(spokenMsg);
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
				spokenMsg.clearAllReceiver();
				
				try {
					Utilisateur us=(Utilisateur)msg.getContentObject();
					System.out.println(us.getEmail_U());
					spokenMsg.setConversationId("repondAuth");
					spokenMsg.addReceiver(msg.getSender());
					Utilisateur u=fon.insription(us);
					
					if(u!=null)
					{
						u=fon.authentifier(u);
						AID send=msg.getSender();
						spokenMsg.clearAllReceiver();
						String convId = "C-" + send.getLocalName();
						
						
						spokenMsg.addReceiver(send);
						System.out.println("ssssssss"+" "+convId+" "+send.getHap());
						i++;
						ContainerBroker.createag(u.getNom_U()+" "+u.getPrenom_U());
							//**	u.setListArticle(null);
							 //**   u.setListVent(null);		
						
					}
					
					spokenMsg.setContentObject(u);
					send(spokenMsg);
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
	
	
	private class AddMarkPlace extends CyclicBehaviour
	{
		int i=0;
		AgentContainer agentContainer;
		private MessageTemplate temp = MessageTemplate
				.MatchConversationId("addmarkplace");
		public AddMarkPlace(Agent a){super(a);
		
		}
		@Override
		public void action() {

			ACLMessage msg = myAgent.receive(temp);
			if (msg != null) {
			
				try
				{
				System.out.println("addmarkplace");
				Markeplace markplace=(Markeplace)msg.getContentObject();
				spokenMsg.clearAllReceiver();
				AID send=msg.getSender();
				spokenMsg.addReceiver(send);
				spokenMsg.setConversationId("addmarkplaceRep");
				Boolean res=fon.addMarkPlace(markplace);
				
				spokenMsg.setContent(fon.addMarkPlace(markplace)+"");
				send(spokenMsg);
				
			
				
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
	public void listVendeur()
	{
	listVendeur.setVisible(true);
	ArrayList<Utilisateur> listven=fon.listVendeur();
	listVendeur.listvendeur=listven;
	listVendeur.addTable();
	}
	
}