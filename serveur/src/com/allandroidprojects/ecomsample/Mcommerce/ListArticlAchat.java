package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;
import java.util.ArrayList;

public class ListArticlAchat implements Serializable {

	private String codPost;
	private ArrayList<Article>listarticle=new ArrayList<Article>();
	public String getCodPost() {
		return codPost;
	}
	public void setCodPost(String codPost) {
		this.codPost = codPost;
	}
	public ArrayList<Article> getListarticle() {
		return listarticle;
	}
	public void setListarticle(ArrayList<Article> listarticle) {
		this.listarticle = listarticle;
	} 
}


