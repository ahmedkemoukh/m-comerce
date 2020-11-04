package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class Resulta implements Serializable {

	private String action;
	private ArrayList<Article> list;
	private String codPost;
	
	public String getCodPost() {
		return codPost;
	}
	public void setCodPost(String codPost) {
		this.codPost = codPost;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public ArrayList<Article> getList() {
		return list;
	}
	public void setList(ArrayList<Article> list) {
		this.list = list;
	}
	
	
	
}
