package com.framework.entity;

import etu1748.framework.annotation.*;
import java.sql.Date;

@Table("employe")
public class Employe {
	@ForeignKey("id_poste")
	Poste poste;
	@Column("nom")
	String nom;
	@Column("prenom")
	String prenom;
	@Id
	@Column("id_employe")
	Integer idEmploye;
	@Column("date_naissance")
	Date dateNaissance;


	public Employe(){}
	public Poste getPoste(){
		return this.poste;
	}
	public void setPoste(Poste poste){
		this.poste = poste;
	}
	public String getNom(){
		return this.nom;
	}
	public void setNom(String nom){
		this.nom = nom;
	}
	public String getPrenom(){
		return this.prenom;
	}
	public void setPrenom(String prenom){
		this.prenom = prenom;
	}
	public Integer getIdEmploye(){
		return this.idEmploye;
	}
	public void setIdEmploye(Integer idEmploye){
		this.idEmploye = idEmploye;
	}
	public Date getDateNaissance(){
		return this.dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance){
		this.dateNaissance = dateNaissance;
	}

}
