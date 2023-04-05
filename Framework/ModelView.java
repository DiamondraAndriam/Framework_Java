package etu1748.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;

    // getters & setters
    public String getView() {
        return this.view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    // constructeur
    public ModelView(String view) {
        setView(view);
    }

    // fonctions
    public void addItem(String key, Object valeur) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, valeur);
    }

}