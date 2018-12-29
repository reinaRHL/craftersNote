package com.mp.rena.craftersnote;

public class Item {

    String name;
    int recipeID;
    int itemID;
    String description;
    String icon;
    String url;

    public Item(String name, int recipeID, String icon, String url) {
        this.name = name;
        this.recipeID = recipeID;
        this.icon = icon;
        this.url = url;
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
