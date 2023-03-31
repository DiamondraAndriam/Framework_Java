package modele;

import etu1748.framework.annotation.Urls;

public class Emp {
    int id;
    String nom;

    @Urls("emp-all")
    public void findAll() {
    }

    @Urls("amp-add")
    public void add() {
    }
}
