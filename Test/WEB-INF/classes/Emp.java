package modele;

import etu1748.framework.annotation.Urls;

import java.util.ArrayList;
import java.util.List;

import etu1748.framework.ModelView;

public class Emp {
    int id;
    String nom;

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // constructeur
    public Emp() {
    }

    public Emp(int id, String nom) {
        setId(id);
        setNom(nom);
    }

    // fonctions
    @Urls("emp-all")
    public ModelView findAll() {
        ModelView mv = new ModelView("tous_emp.jsp");
        List<Emp> l = new ArrayList<>();
        l.add(new Emp(1, "Rabe"));
        l.add(new Emp(2, "Rakoto"));
        l.add(new Emp(3, "Rasoa"));
        l.add(new Emp(4, "Andry"));
        mv.addItem("liste", l);
        return mv;
    }

    @Urls("emp-add")
    public ModelView add() {
        return new ModelView("add_emp.jsp");
    }
}
