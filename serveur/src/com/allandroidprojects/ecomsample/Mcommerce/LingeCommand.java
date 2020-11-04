package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
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
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	private int quant;
	@ManyToOne
	@JoinColumn(name="article")
	private Article article;
	
	@ManyToOne
	@JoinColumn(name="command_id")

	private Command command;

	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	
}
