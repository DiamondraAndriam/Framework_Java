package modele;

import etu1748.framework.annotation.Urls;
import etu1748.framework.ModelView;

public class Emp {
    int id;
    String nom;

    @Urls("emp-all")
    public ModelView findAll() {
        return new ModelView("tous_emp.jsp");
    }

    @Urls("amp-add")
    public ModelView add() {
        return new ModelView("add_emp.jsp");
    }

    public Emp(){}
}
