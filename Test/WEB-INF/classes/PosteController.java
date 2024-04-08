package com.framework.controller;

import com.framework.entity.Poste;
import etu1748.framework.annotation.*;
import etu1748.framework.*;

import java.util.List;

@MVCController

public class PosteController {
	private Poste poste = new Poste();

	@Urls("poste/new.do")
	@JSON("POST")
	public ResponseAPI save(@Param Poste poste){
	 	try {
		List<Object> data = GenericDAO.insert(poste);
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("poste/update.do")
	@JSON("PUT")
	public ResponseAPI update(@Param Poste poste){
	 	try {
		List<Object> data = GenericDAO.update(poste, poste.getIdPoste());
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("poste/delete.do")
	@JSON("DELETE")
	public ResponseAPI delete(@Param Poste poste){
	 	try {
		List<Object> data = GenericDAO.delete(poste, poste.getIdPoste());
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}
	@Urls("poste/find.do")
	@JSON("GET")
	public ResponseAPI findAll(){
	 	try {
		List<Object> data = GenericDAO.findAll(poste);
		return new ResponseAPI(data, null);
		} catch (Exception e) {
		return new ResponseAPI(null, e.getMessage());
		}
	}


}
