package com.mp.rena.craftersnote;

import java.util.ArrayList;

public class Item {

    String name;
    int id;
    int quantity;
    String description;
    String icon;
    String url;
    String urlType;
    String job;
    String jobLevel;
    static ArrayList<Item> reqMaterialList = new ArrayList<>();

    public Item(){

    }

    public Item(String name, int recipeID, String icon, String url, String urlType) {
        this.name = name;
        this.id = recipeID;
        this.icon = icon;
        this.url = url;
        this.urlType = urlType;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlType(String urlType){
        this.urlType = urlType;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrlType() {
        return this.urlType;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Item){
            Item p = (Item) obj;
            return this.id == p.id;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result;

        if (String.valueOf(this.id) != null){
            result += String.valueOf(this.id).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return name + " (" + quantity + ")";
    }


}
