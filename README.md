# Framework_Java

## Installation et configuration

- Ajoutez le fichier framework.jar dans lib de WEB-INF dans TomCat 8.x.x ou GlassFish 4.x.x

- Veuillez configurer votre fichier web.xml dans WEB-INF en ajoutant le code suivant:

<code>

    <servlet>
      <servlet-name>FrontServlet</servlet-name>
      <servlet-class>etu1748.framework.servlet.FrontServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
      <servlet-name>FrontServlet</servlet-name>
      <url-pattern>/</url-pattern>
    </servlet-mapping>

</code>

    -  Si vous n'arrivez pas à compiler vos classes, ajoutez framework.jar à votre classpath.

## Mode d'emploi

- Créer vos objets avec des getters et setters:
<code>
    public class Emp {
        private int id;
        private String nom;
        private Date date_naissance;
        ...
    }
</code>

- Si votre classe a plusieurs constructeurs, n'oubliez pas d'ajouter un constructeur vide.

- Annotez @Urls (package etu1748.framework.annotation) les méthodes dans vos classes qui redirigent vers des pages :
    * toutes les méthodes retourne un objet ModelView (package etu1748.framework)
<code>
    public class Votre_classe {
        
        @Urls("votre_url")
        public ModelView votre_fonction() {
            return new ModelView("votre_page.jsp");
        }
    }
</code>

    * pour ajouter des attributs, on peut ajouter des attributs au HttpServletRequest à travers le ModelView mv par la méthode mv.addItem
    
<code>
    public class Votre_classe {
        @Urls("votre_url")
        public ModelView votre_fonction() {
            mv.addItem(nom_de_l_attribut, valeur_de_l_attribut);
            ModelView mv =  new ModelView("votre_page.jsp");
            return mv;
        }
    }
</code>

    * pour récupérer les attributs dans la page de redirection, on prend les attributs du HttpServletRequest au même nom qu'à travers le ModelView:

<code>    
    <% T votre_objet = (T) request.getAttribute("nom_attribut"); %>
</code>

    * pour sauvegarder des objets depuis un formulaire, nommez votre fonction save() et envoyer l'objet comme attribut:

<code>
    public class Votre_classe {
        
        @Urls("nom_url")
        public ModelView save() {
            ModelView mv = new ModelView("votre_page.jsp");
            mv.addItem("nom_attribut", this);
            return mv;
        }
    }

    <% Votre_classe objet = (Votre_classe) request.getAttribute("nom_attribut"); %>
</code>

    Remarque: Ce framework ne prend en charge que les types : int, float, double, boolean, Integer, Float, Double, Boolean, Date, LocalDate, String en attribut d'objet pour la fonction save().
