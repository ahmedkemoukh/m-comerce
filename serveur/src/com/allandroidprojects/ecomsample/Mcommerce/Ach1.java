package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Ach1 implements Serializable {
	
	public String toString() {
		return "Article [Id_P=" + Id_P + ", Nom_p=" + Nom_p + ", Prix_P=" + Prix_P + ", quanSt_p=" + quanSt_p
				+ ", description_p=" + description_p + ", Imag_p="  + ", Marque_p=" + Marque_p + ", libCat_p="
				+ libCat_p + "]";
	}
	
	private int Id_P;
	private String Nom_p;
	
	private int Prix_P;
	
	private int quanSt_p;

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}
	
	private String description_p;

	
    private	Set<String> Imags_p=new HashSet<String>();
	
	public int getId_P() {
		return Id_P;
	}
	
	public void setId_P(int id_P) {
		Id_P = id_P;
	}
	
	public String getNom_p() {
		return Nom_p;
	}

	public void setNom_p(String nom_p) {
		Nom_p = nom_p;
	}

	public int getPrix_P() {
		return Prix_P;
	}

	public void setPrix_P(int prix_P) {
		Prix_P = prix_P;
	}
	
	public int getQuanSt_p() {
		return quanSt_p;
	}
	
	public void setQuanSt_p(int quanSt_p) {
		this.quanSt_p = quanSt_p;
	}
	public String getDescription_p() {
		return description_p;
	}
	
	public void setDescription_p(String description_p) {
		this.description_p = description_p;
	}
	
	
	public Set<String> getImags_p() {
		return Imags_p;
	}
	
	public void setImags_p(Set<String> imags_p) {
		Imags_p = imags_p;
	}
	
	public String getMarque_p() {
		return Marque_p;
	}

	public void setMarque_p(String marque_p) {
		Marque_p = marque_p;
	}
	
	public String getLibCat_p() {
		return libCat_p;
	}

	public void setLibCat_p(String libCat_p) {
		this.libCat_p = libCat_p;
	}

	private String Marque_p;

	private String libCat_p;

	private Utilisateur user;
}




