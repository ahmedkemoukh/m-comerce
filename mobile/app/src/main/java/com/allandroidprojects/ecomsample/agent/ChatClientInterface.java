package com.allandroidprojects.ecomsample.agent;

import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.ListArticlAchat;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;

import java.util.ArrayList;

/**
 * This interface implements the logic of the chat client running on the user
 * terminal.
 * 
 * @author Michele Izzo - Telecomitalia
 */

public interface ChatClientInterface {
	public void handleSpoken(String s);
	public String[] getParticipantNames();
	public void DemandArticle (String s,int curentype);

	public void recherchArticle(ArticlRech a);
	public void AchatArt(Command a);

	public void autentifier(Utilisateur u);
	public void inscription(Utilisateur u);
	public void addproduit(Article a);
	public void suparticle(Article a);
	public void listAchat();
	public void listVend();
	public void addMarkPlace(Markeplace getMarkplace);
	public void changeEtat(String a);

}