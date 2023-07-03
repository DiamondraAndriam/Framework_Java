package etu1748.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;
    HashMap<String, Object> session;

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

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
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

    public void addSession(String key, Object valeur) {
        if (this.session == null) {
            this.session = new HashMap<String, Object>();
        }
        this.session.put(key, valeur);
    }
}