package com.allandroidprojects.ecomsample.Mcommerce;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Entity
public class Compte implements Serializable {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int cod_post;
private String password;
private float mony;
public int getCod_post() {
	return cod_post;
}
public void setCod_post(int cod_post) {
	this.cod_post = cod_post;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public float getMony() {
	return mony;
}
public void setMony(float mony) {
	this.mony = mony;
}

@OneToOne
@JoinColumn(name = "user_id")
private Utilisateur userCompte;
public Utilisateur getUser() {
	return userCompte;
}
public void setUser(Utilisateur user) {
	this.userCompte = user;
}

}
