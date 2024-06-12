package com.framework.entity;

import etu1748.framework.annotation.*;
import java.util.List;

@Table("poste")
public class Poste {
	@Id
	@Column("id_poste")
	Integer idPoste;
	@Column("salaire")
	Double salaire;
	@Column("nom_poste")
	String nomPoste;


	public Poste(){}
	public Integer getIdPoste(){
		return this.idPoste;
	}
	public void setIdPoste(Integer idPoste){
		this.idPoste = idPoste;
	}
	public Double getSalaire(){
		return this.salaire;
	}
	public void setSalaire(Double salaire){
		this.salaire = salaire;
	}
	public String getNomPoste(){
		return this.nomPoste;
	}
	public void setNomPoste(String nomPoste){
		this.nomPoste = nomPoste;
	}

}
