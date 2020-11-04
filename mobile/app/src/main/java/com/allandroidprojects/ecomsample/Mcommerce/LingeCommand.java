package com.allandroidprojects.ecomsample.Mcommerce;


import java.io.Serializable;

public class LingeCommand implements Serializable {

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuant() {
		return quant;
	}
	public void setQuant(int quant) {
		this.quant = quant;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}

	private int id;
	private int quant;
	private Article article;
	


	private Command command;

	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	
}
