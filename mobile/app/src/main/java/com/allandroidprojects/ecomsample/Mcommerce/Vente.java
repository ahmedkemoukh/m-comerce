package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.sql.Date;


public class Vente implements Serializable {

	private int id_v;
	public int getId_v() {
		return id_v;
	}

	public void setId_v(int id_v) {
		this.id_v = id_v;
	}


	private Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}




	private Utilisateur user;


	private Article article;
	private  String messageerror;
	private int etat;

	public String getMessageerror() {
		return messageerror;
	}

	public void setMessageerror(String messageerror) {
		this.messageerror = messageerror;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	private int quan;

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public int getQuan() {
		return quan;
	}

	public void setQuan(int quan) {
		this.quan = quan;
	}
}
