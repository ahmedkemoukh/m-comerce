package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class Markeplace implements Serializable{

	public int getEtatMarkplace() {
		return etatMarkplace;
	}

	public void setEtatMarkplace(int etatMarkplace) {
		this.etatMarkplace = etatMarkplace;
	}

	private int etatMarkplace;
	private int id;
	private String name;
	private String type;
	private String position;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	

	
	private Collection<Article> listArticle=new ArrayList<Article>();
	

	public Collection<Article> getListArticle() {
	return listArticle;
}
public void setListArticle(Collection<Article> listArticle) {
	this.listArticle = listArticle;
}

	@Override
public String toString() {
	return "Markeplace [id=" + id + ", name=" + name + ", type=" + type + ", position=" + position + ", listArticle="
			+ listArticle + ", user=" + user + "]";
}


	private Utilisateur user;
	public Utilisateur getUser() {
		return user;
	}
	public void setUser(Utilisateur user) {
		this.user = user;
	}
}
