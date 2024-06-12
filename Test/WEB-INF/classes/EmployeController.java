package com.framework.controller;

import com.framework.entity.Employe;
import etu1748.framework.annotation.*;
import etu1748.framework.*;

import java.util.List;

@MVCController

public class EmployeController {
	private Employe employe = new Employe();
	private Pagination pagination;

	@Urls("employe/new.do")
	@JSON("POST")
	public ResponseAPI save(@Param Employe employe) {
		try {
			List<Object> data = GenericDAO.insert(employe);
			return new ResponseAPI(data, null);
		} catch (Exception e) {
			return new ResponseAPI(null, e.getMessage());
		}
	}

	@Urls("employe/update.do")
	@JSON("PUT")
	public ResponseAPI update(@Param Employe employe) {
		try {
			List<Object> data = GenericDAO.update(employe, employe.getIdEmploye());
			return new ResponseAPI(data, null);
		} catch (Exception e) {
			return new ResponseAPI(null, e.getMessage());
		}
	}

	@Urls("employe/delete.do")
	@JSON("DELETE")
	public ResponseAPI delete(@Param Employe employe) {
		try {
			List<Object> data = GenericDAO.delete(employe, employe.getIdEmploye());
			return new ResponseAPI(data, null);
		} catch (Exception e) {
			return new ResponseAPI(null, e.getMessage());
		}
	}

	@Urls("employe/find.do")
	@JSON("GET")
	@Paginate
	public ResponseAPI findAll() {
		try {
			List<Object> data = GenericDAO.findAll(employe, pagination);
			return new ResponseAPI(data, null);
		} catch (Exception e) {
			return new ResponseAPI(null, e.getMessage());
		}
	}

}
