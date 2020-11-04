package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity

public class Article implements Serializable {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int Id_P;
	@Transient
	 private ArrayList<byte[]>listImage;
	public ArrayList<byte[]> getListImage() {
		return listImage;
	}

	public void setListImage(ArrayList<byte[]> listImage) {
		this.listImage = listImage;
	}

	@Override
	public String toString() {
		return "Article [Id_P=" + Id_P + ", Nom_p=" + Nom_p + ", Prix_P=" + Prix_P + ", quanSt_p=" + quanSt_p
				+ ", description_p=" + description_p + ", Imag_p="  + ", Marque_p=" + Marque_p + ", libCat_p="
				+ libCat_p + "type="+type+ "]";
	}
	

	private String Nom_p;
	
	private int Prix_P;
	
	private int quanSt_p;
	private int type=0;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	
	public Markeplace getMarkeplace() {
		return markeplace;
	}

	public void setMarkeplace(Markeplace markeplace) {
		this.markeplace = markeplace;
	}


	private String description_p;
    
	 
	@ElementCollection
	@CollectionTable(name="Images", joinColumns=@JoinColumn(name="Id_P"))
	@Column(name="image")

    private	Collection<String> Imags_p=new ArrayList<String>();
	
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
	
	
	public Collection<String> getImags_p() {
		return Imags_p;
	}
	
	public void setImags_p(Collection<String> imags_p) {
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
     
	@ManyToOne
	@JoinColumn(name="markeplace_id")
	
	private Markeplace markeplace;
	
	
	
	
	

}
