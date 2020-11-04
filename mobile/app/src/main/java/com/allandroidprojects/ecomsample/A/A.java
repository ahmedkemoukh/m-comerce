package com.allandroidprojects.ecomsample.A;

import java.io.Serializable;

public class A implements Serializable{

    private String marque;
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