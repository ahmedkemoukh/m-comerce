package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;

public class ArticlRech implements Serializable{


	private String nom;
	private String gategori;
	private String marque;
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getGategori() {
		return gategori;
	}
	public void setGategori(String gategori) {
		this.gategori = gategori;
	}
	private int prix;
	public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}


}
