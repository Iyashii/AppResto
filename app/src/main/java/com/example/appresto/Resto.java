package com.example.appresto;

public class Resto {
    protected Integer numresto;
    protected String nomR;
    protected String villeR;

    public Resto(Integer numresto, String nomR, String villeR) {

        this.nomR = nomR;
        this.villeR = villeR;
        this.numresto= numresto;

    }


    @Override


    public String toString() {
        return nomR + "   " + villeR;
    }

    public String getNomR() {
        return nomR;
    }

    public void setNomR(String nomR) {
        this.nomR = nomR;
    }

    public String getVilleR() {
        return villeR;
    }

    public void setVilleR(String villeR) {
        this.villeR = villeR;
    }

    public Integer getNumresto() {
        return numresto;
    }

    public void setNumresto(Integer numresto) {
        this.numresto = numresto;
    }
}
