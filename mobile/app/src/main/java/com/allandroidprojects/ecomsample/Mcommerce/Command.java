package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;



public class Command implements Serializable {

	private int id_C;



	private String Etatpayment;

	public String getEtatpayment() {
		return Etatpayment;
	}

	public void setEtatpayment(String etatpayment) {
		Etatpayment = etatpayment;
	}

	private String numberPhon;
	private String Location;
	public String getNumberPhon() {
		return numberPhon;
	}

	public void setNumberPhon(String numberPhon) {
		this.numberPhon = numberPhon;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}


	private Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}



	private Utilisateur achteur;



	private Utilisateur vendeur;

	/*@ManyToOne
	@JoinColumn(name="Article_id")
	private Article article;*/
	private  String messageerror;

	private String etat="attand";

	public String getMessageerror() {
		return messageerror;
	}

	public void setMessageerror(String messageerror) {
		this.messageerror = messageerror;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}





	public Utilisateur getAchteur() {
		return achteur;
	}

	public void setAchteur(Utilisateur achteur) {
		this.achteur = achteur;
	}

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}



	private Collection<LingeCommand> lignsCommand=new ArrayList<LingeCommand>();
	public int getId_C() {
		return id_C;
	}

	public void setId_C(int id_C) {
		this.id_C = id_C;
	}

	public Collection<LingeCommand> getLignsCommand() {
		return lignsCommand;
	}

	public void setLignsCommand(Collection<LingeCommand> lignsCommand) {
		this.lignsCommand = lignsCommand;
	}

	public float payTotal()
	{
		float pay=0;
		for(LingeCommand l:getLignsCommand())
		{
			pay=pay+l.getQuant()*l.getArticle().getPrix_P();
		}

		return pay;
	}

}




