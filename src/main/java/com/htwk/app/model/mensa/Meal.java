package com.htwk.app.model.mensa;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
 
import com.google.gson.annotations.SerializedName;
 
public class Meal implements Serializable {
 
    /**
     * 
     */
    private static final long serialVersionUID = -7140574727302014457L;
 
    private String title;
 
    private Collection<String> ingredients;
 
    private Map<Integer, String> addititves;
 
    private Map<Integer, Double> price;
 
    /**
     * @return the title
     */
    public synchronized final String getTitle() {
        return title;
    }
 
    /**
     * @param title the title to set
     */
    public synchronized final void setTitle(String title) {
        this.title = title;
    }
 
    /**
     * @return the ingredients
     */
    public synchronized final Collection<String> getIngredients() {
        if(ingredients == null){
            ingredients = new ArrayList<String>();
        }
        return ingredients;
    }
 
    /**
     * @param ingredients the ingredients to set
     */
    public synchronized final void setIngredients(Collection<String> ingredients) {
        this.ingredients = ingredients;
    }
 
    /**
     * @return the addititves
     */
    public synchronized final Map<Integer, String> getAddititves() {
        if(addititves == null){
            addititves = new HashMap<Integer, String>();
        }
        return addititves;
    }
 
    /**
     * @param addititves the addititves to set
     */
    public synchronized final void setAddititves(Map<Integer, String> addititves) {
        this.addititves = addititves;
    }
 
    /**
     * @return the price
     */
    public synchronized final Map<Integer, Double> getPrice() {
        if(price == null){
            price = new HashMap<Integer, Double>();
        }
        return price;
    }
 
    /**
     * @param price the price to set
     */
    public synchronized final void setPrice(Map<Integer, Double> price) {
        this.price = price;
    }
}