# Framework_Java

## Prérequis

- Le fichier framework-jdk8-tomcat8.jar
- Environnement Java JDK8 ou OpenJDK8
- Serveur TomCat 8.x.x ou GlassFish 4.x.x

## Installation et configuration

- Ajoutez le fichier framework-jdk8-tomcat8.jar dans lib de WEB-INF de votre

- Veuillez configurer votre fichier web.xml dans WEB-INF en ajoutant le code suivant:

    <servlet>
      <servlet-name>FrontServlet</servlet-name>
      <servlet-class>etu1748.framework.servlet.FrontServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
      <servlet-name>FrontServlet</servlet-name>
      <url-pattern>/</url-pattern>
    </servlet-mapping>


    -  Si vous n'arrivez pas à compiler vos classes, ajoutez framework.jar à votre classpath.

## Mode d'emploi

- Créer vos objets avec des getters et setters:

    public class VotreClasse {
        private int id;
        private String nom;
        private Date date_naissance;
        ...
    }

- Si votre classe a plusieurs constructeurs, n'oubliez pas d'ajouter un constructeur vide.

- Annotez @Urls (package etu1748.framework.annotation) les méthodes dans vos classes qui redirigent vers des pages :
    * toutes les méthodes retourne un objet ModelView (package etu1748.framework)

    public class VotreClasse {
        
        @Urls("votre_url")
        public ModelView votre_fonction() {
            return new ModelView("votre_page.jsp");
        }
    }


    * pour ajouter des attributs, on peut ajouter des attributs au HttpServletRequest à travers le ModelView mv par la méthode mv.addItem
    
    public class VotreClasse {
        @Urls("votre_url")
        public ModelView votre_fonction() {
            mv.addItem(nom_de_l_attribut, valeur_de_l_attribut);
            ModelView mv =  new ModelView("votre_page.jsp");
            return mv;
        }
    }


    * pour récupérer les attributs dans la page de redirection, on prend les attributs du HttpServletRequest au même nom qu'à travers le ModelView:

    
    <% T votre_objet = (T) request.getAttribute("nom_attribut"); %>


    * pour sauvegarder des objets depuis un formulaire, nommez votre fonction save() et envoyer l'objet comme attribut:

    public class VotreClasse {
        
        @Urls("nom_url")
        public ModelView save() {
            ModelView mv = new ModelView("votre_page.jsp");
            mv.addItem("nom_attribut", this);
            return mv;
        }
    }

    <% VotreClasse objet = (Votre_classe) request.getAttribute("nom_attribut"); %>

    Remarque: Ce framework ne prend en charge que les types : int, float, double, boolean, Integer, Float, Double, Boolean, Date, LocalDate, String en attribut d'objet pour la fonction save().

    * pour envoyer des paramètres à travers les liens, vous avez plusieurs choix:
        - Annotez la fonction vers le ModelView avec @ParamList et donnez en liste les noms des paramètres

    @Urls("votre_url")
    @ParamList({"argument_1","argument_2"})
    public ModelView fonction(T argument_1, T1 argument_2) {
        ModelView mv = new ModelView("votre_page.jsp");
        mv.addItem("argument_1", argument_1);
        mv.addItem("argument_2", argument_2);
        return mv;
    }

        - Annotez chaque paramètre de votre fonction avec @Param 

    @Urls("votre_url")
    public ModelView fonction(@Param("argument_1") T argument_1, @Param("argument_2") T1 argument_2) {
        ModelView mv = new ModelView("votre_page.jsp");
        mv.addItem("argument_1", argument_1);
        mv.addItem("argument_2", argument_2);
        return mv;
    }

        - Si vous n'utilisez pas d'IDE, pour compiler votre classe, utilisez la commande suivante:

    javac -parameters VotreClasse.java

    Remarque: n'oubliez pas d'ajouter en item du ModelView les attributs