package com.framework.test.controller;

import com.framework.test.modele.Employe;
import etu1748.framework.annotation.*;
import etu1748.framework.*;


public class EmployeController
 {

	private Employe employe;


	@Urls("employe/new")
	@JSON
	@RequestMethod("POST")
	public ResponseAPI save(@Param Employe employe){
	 	try {
		List<Object> data = GenericDAO.insert(employe);
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("employe/update")
	@JSON
	@RequestMethod("PUT")
	public ResponseAPI update(@Param Employe employe){
	 	try {
		List<Object> data = GenericDAO.update(employe, employe.getId());
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("employe/delete")
	@JSON
	@RequestMethod("DELETE")
	public void delete(@Param Employe employe){
	 	try {
		List<Object> data = GenericDAO.delete(employe, employe.getId());
		    return new ResponseAPI(data, null);
		} catch (Exception e) {
		    return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("employe")
	@JSON
	@RequestMethod("GET")
	public ResponseAPI findAll(){
	 	try {
		List<Object> data = GenericDAO.findAll(employe);
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("employe")
	@JSON
	@RequestMethod("GET")
	public ResponseAPI findById(@Param Employe employe){
	 	try {
		List<Object> data = GenericDAO.findById(employe, employe.getId());
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}





}
