package modele;

import etu1748.framework.annotation.*;
import etu1748.framework.FileUpload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;

import etu1748.framework.ModelView;

@Scope("singleton")
public class Emp {
    private int id;
    private String nom;
    private String prenom;
    private Date date_naissance;
    private String login;
    private String password;
    private FileUpload badge;
    private HashMap<String, Object> session;

    // getters & setters
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }

    public FileUpload getBadge() {
        return badge;
    }

    public void setBadge(FileUpload badge) {
        this.badge = badge;
    }

    public HashMap getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public void addSession(String key, Object value) {
        if (this.session == null) {
            this.session = new HashMap<String, Object>();
        }
        this.session.put(key, value);
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

    @Urls("emp-save")
    public ModelView save() {
        ModelView mv = new ModelView("sauvegarde.jsp");
        mv.addItem("employe", this);
        return mv;
    }

    @Urls("details-emp")
    @ParamList({ "id", "nom" })
    public ModelView details(@Param("id") int id, @Param("nom") String nom) {
        System.out.println(id);
        System.out.println(nom);
        ModelView mv = new ModelView("details.jsp");
        mv.addItem("id", id);
        mv.addItem("nom", nom);
        return mv;
    }

    // sprint 9
    @Urls("upload")
    public ModelView upload() {
        ModelView mv = new ModelView("done.jsp");
        mv.addItem("emp", this);
        return mv;
    }

    // sprint 10
    @Urls("test-singleton")
    public ModelView singleton() {
        return new ModelView("test_singleton1.jsp");
    }

    // sprint 11
    @Urls("authentification")
    public ModelView authenticate() {
        ModelView mv = new ModelView("accueil.jsp");
        mv.addItem("Emp", this);
        // System.out.println(id);
        // System.out.println(nom);
        if (this.login.equals("Nom") && this.password.equals("123456") ||
                this.login.equals("Admin") && this.password.equals("123456")) {
            mv.addSession("isConnected", true);
            if (this.login.equals("Admin")) {
                mv.addSession("profil", "admin");
            }
        }
        return mv;
    }

    // sprint 11
    @Auth("admin")
    @Urls("admin")
    public ModelView adminView() {
        return new ModelView("admin.jsp");
    }

    // sprint 11
    @Auth
    @Urls("profil")
    public ModelView profilView() {
        return new ModelView("profil.jsp");
    }

    // sprint 12
    @Session
    @Urls("info_session")
    public ModelView addpseudo() {
        ModelView mv = new ModelView("session.jsp");
        mv.addItem("emp", this);
        return mv;
    }

    // sprint 13
    @Urls("get_emps")
    public ModelView getEmp() {
        ModelView mv = new ModelView("tous_emp.jsp");
        List<Emp> l = new ArrayList<>();
        l.add(new Emp(1, "Rabe"));
        l.add(new Emp(2, "Rakoto"));
        l.add(new Emp(3, "Rasoa"));
        l.add(new Emp(4, "Andry"));
        mv.addItem("liste", l);
        mv.setJSON(true);
        return mv;
    }

    // sprint 14
    @JSON
    @Urls("all_emps")
    public List<Emp> listEmp() {
        List<Emp> l = new ArrayList<>();
        l.add(new Emp(1, "Rabe"));
        l.add(new Emp(2, "Rakoto"));
        l.add(new Emp(3, "Rasoa"));
        l.add(new Emp(4, "Andry"));
        return l;
    }

}
