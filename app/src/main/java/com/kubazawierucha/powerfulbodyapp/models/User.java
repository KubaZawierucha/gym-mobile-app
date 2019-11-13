package com.kubazawierucha.powerfulbodyapp.models;

public class User {

    private String name;
    private int age;
    private String gender;
    private double height;
    private double weight;
    private double waist;
    private double neck;
    private double hip;
    private boolean isCreatedByApp;

    public User(String name, int age, String gender, double height, double weight, double waist, double neck, double hip, boolean isCreatedByApp) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
        this.neck = neck;
        this.hip = hip;
        this.isCreatedByApp = isCreatedByApp;
    }

    public User(boolean isCreatedByApp) {
        this.isCreatedByApp = isCreatedByApp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getNeck() {
        return neck;
    }

    public void setNeck(double neck) {
        this.neck = neck;
    }

    public double getHip() {
        return hip;
    }

    public void setHip(double hip) {
        this.hip = hip;
    }

    public boolean isCreatedByApp() {
        return isCreatedByApp;
    }

    public void setCreatedByApp(boolean createdByApp) {
        isCreatedByApp = createdByApp;
    }
}
