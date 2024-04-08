import java.sql.Date;

public class Employe {
    public Employe() {
    }

    public Employe(String nom, String prenom, Date date_naissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
    }

    String nom;
    String prenom;
    Date date_naissance;

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

}
