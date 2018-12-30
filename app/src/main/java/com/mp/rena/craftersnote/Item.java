package com.mp.rena.craftersnote;

import java.util.ArrayList;

public class Item {

    String name;
    int recipeID;
    int itemID;
    String description;
    String icon;
    String url;
    String urlType;
    String job;
    String jobLevel;
    ArrayList<Item> reqMaterialList = new ArrayList<>();

    public Item(String name, int recipeID, String icon, String url, String urlType) {
        this.name = name;
        this.recipeID = recipeID;
        this.icon = icon;
        this.url = url;
        this.urlType = urlType;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Item){
            Item p = (Item) obj;
            return this.recipeID == p.recipeID;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result;

        if (String.valueOf(this.recipeID) != null){
            result += String.valueOf(this.recipeID).hashCode();
        }
        return result;
    }
}
