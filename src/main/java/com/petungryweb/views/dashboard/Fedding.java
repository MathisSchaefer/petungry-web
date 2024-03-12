package com.petungryweb.views.dashboard;

/**
 * Simple DTO class for the inbox list to demonstrate complex object data
 */
public class Fedding {

    private Status status;


    private String fuetterer;

    private double menge;


    private String theme;

    enum Status {
        EXCELLENT, OK, FAILING;
    }

    public Fedding() {

    }

    public Fedding(Status status, String fuetterer, double menge) {
        this.status = status;
        this.fuetterer = fuetterer;
        this.menge = menge;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFuetterer() {
        return fuetterer;
    }

    public void setFuetterer(String fuetterer) {
        this.fuetterer = fuetterer;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }
}
