package modele;

import etu1748.framework.annotation.*;
import etu1748.framework.FileUpload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import api.ResponseAPI;
import dao.GenericDAO;

import java.sql.Date;

import etu1748.framework.ModelView;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private Date date_naissance;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }

    @Urls("employe")
    @RequestMethod("GET")
    @JSON
    public List<Employe> findAll() {
        return null;
    }

    @Urls("employe/id")
    @RequestMethod("GET")
    @JSON
    public List<Employe> findById() {
        return null;
    }

    @Urls("employe/update")
    @RequestMethod("PUT")
    @JSON
    public ResponseAPI update() {
        GenericDAO.update(this, this.getId());
    }

    @Urls("employe/delete")
    @RequestMethod("DELETE")
    @JSON
    public ResponseAPI delete() {
        try {
            GenericDAO.delete(this, this.getId());
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    @Urls("employe/new")
    @RequestMethod("POST")
    @JSON
    public ResponseAPI insert() {
        try {
            GenericDAO.insert(this);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
