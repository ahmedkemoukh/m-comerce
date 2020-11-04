package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class Utilisateur implements Serializable {






	private int id_U;
	private int etat;
	private String Nom_U;
	private String Prenom_U;
	private int type=0;
	private String Sexe_U;
	private String NumperPhone_U;
	private String location_U;

   private String typeProduit;
   
	

  private Compte compte;
	

	private Markeplace markplace;
	
	  public Markeplace getMarkplace() {
		return markplace;
	}
	public void setMarkplace(Markeplace markplace) {
		this.markplace = markplace;
	}




	private Collection<Command> listVentAchteur=new ArrayList<Command>();

	private Collection<Command> listVentVendeur=new ArrayList<Command>();
	
	
	public int getEtat() {
		return etat;
	}
	public void setEtat(int etat) {
		this.etat = etat;
	}


	


	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}





	private String Email_U;
	private String Password_U;
	
	
	

	
	
	
	
	public int getId_U() {
		return id_U;
	}
	public void setId_U(int id_U) {
		this.id_U = id_U;
	}
	public String getNom_U() {
		return Nom_U;
	}
	public void setNom_U(String nom_U) {
		Nom_U = nom_U;
	}
	public String getPrenom_U() {
		return Prenom_U;
	}
	public void setPrenom_U(String prenom_U) {
		Prenom_U = prenom_U;
	}
	public String getSexe_U() {
		return Sexe_U;
	}
	public void setSexe_U(String sexe_U) {
		Sexe_U = sexe_U;
	}
	public String getNumperPhone_U() {
		return NumperPhone_U;
	}
	public void setNumperPhone_U(String numperPhone_U) {
		NumperPhone_U = numperPhone_U;
	}
	public String getLocation_U() {
		return location_U;
	}
	public void setLocation_U(String location_U) {
		this.location_U = location_U;
	}
	public String getTypeProduit() {
		return typeProduit;
	}
	public void setTypeProduit(String typeProduit) {
		this.typeProduit = typeProduit;
	}
	public String getEmail_U() {
		return Email_U;
	}
	public void setEmail_U(String email_U) {
		Email_U = email_U;
	}
	public String getPassword_U() {
		return Password_U;
	}
	public void setPassword_U(String password_U) {
		Password_U = password_U;
	}

	



	
	
	
	

	public Collection<Command> getListVentAchteur() {
		return listVentAchteur;
	}
	public void setListVentAchteur(Collection<Command> listVent) {
		this.listVentAchteur = listVent;
	}
	
	
	
	public Collection<Command> getListVentVendeur() {
		return listVentVendeur;
	}
	public void setListVentVendeur(Collection<Command> listVent) {
		this.listVentVendeur = listVent;
	}
	

	
	public Compte getCompte() {
		return compte;
	}
	public void setCompte(Compte compte) {
		this.compte = compte;
	}
	
	

}
