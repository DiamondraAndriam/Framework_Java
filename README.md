# Framework_Java

## A propos du framework 

### Prérequis

- Le fichier framework.jar
- Environnement Java JDK8 ou OpenJDK8
- Serveur TomCat 8.x.x ou GlassFish 4.x.x

### Installation et configuration

- Ajoutez le fichier framework-jdk8-tomcat8.jar dans lib de WEB-INF de votre projet

- Veuillez configurer votre fichier web.xml dans WEB-INF en ajoutant le code suivant:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
  version="3.1"
  metadata-complete="true">

    <servlet>
      <servlet-name>FrontServlet</servlet-name>
      <servlet-class>etu1748.framework.servlet.FrontServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
      <servlet-name>FrontServlet</servlet-name>
      <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

-  Si vous n'arrivez pas à compiler vos classes, ajoutez framework.jar à votre classpath ou à la librairie du serveur.

## Mode d'emploi

### Vos attributs

- Créer vos objets avec des getters et setters:

```java
    public class VotreClasse {
        private int id;
        private String nom;
        private Date date_naissance;
        ...
    }
```

- Si votre classe a plusieurs constructeurs, n'oubliez pas d'ajouter un constructeur vide.

### Vos fonctions ou méthodes

- Annotez @Urls (package etu1748.framework.annotation) les méthodes dans vos classes qui redirigent vers des pages :
  * les méthodes retournent un objet ModelView (package etu1748.framework):
    
        
```java
    public class VotreClasse {
        @Urls("votre_url")
        public ModelView votre_fonction() {
            return new ModelView("votre_page.jsp");
        }
    }
```

  * pour ajouter des attributs, on peut ajouter des attributs au HttpServletRequest à travers le ModelView mv par la méthode mv.addItem
        
```java
        public class VotreClasse {
            @Urls("votre_url")
            public ModelView votre_fonction() {
                mv.addItem(nom_de_l_attribut, valeur_de_l_attribut);
                ModelView mv =  new ModelView("votre_page.jsp");
                return mv;
            }
        }
```

  * pour récupérer les attributs dans la page de redirection, on prend les attributs du HttpServletRequest au même nom qu'à travers le ModelView:
    
```java
        T votre_objet = (T) request.getAttribute("nom_attribut");
```

  * pour sauvegarder des objets depuis un formulaire, nommez votre fonction save() et envoyer l'objet comme attribut:
        
```java 
public class VotreClasse {
    @Urls("nom_url")
    public ModelView save() {
        ModelView mv = new ModelView("votre_page.jsp");
        mv.addItem("nom_attribut", this);
        return mv;
    }
}
        
VotreClasse objet = (Votre_classe) request.getAttribute("nom_attribut");
```

Remarque: Ce framework ne prend en charge que les types : int, float, double, boolean, Integer, Float, Double, Boolean, Date, LocalDate, String en attribut d'objet pour la fonction save().

  * pour envoyer des paramètres à travers les liens, vous avez plusieurs choix:

    - Annotez la fonction vers le ModelView avec @ParamList et donnez en liste les noms des paramètres
            
```java
    @Urls("votre_url")
    @ParamList({"argument_1","argument_2"})
    public ModelView fonction(T argument_1, T1 argument_2) {
        ModelView mv = new ModelView("votre_page.jsp");
        mv.addItem("argument_1", argument_1);
        mv.addItem("argument_2", argument_2);
        return mv;
    }
```     
     
  - Annotez chaque paramètre de votre fonction avec @Param 
            
```java
    @Urls("votre_url")
    public ModelView fonction(@Param("argument_1") T argument_1, @Param("argument_2") T1 argument_2) {
        ModelView mv = new ModelView("votre_page.jsp");
        mv.addItem("argument_1", argument_1);
        mv.addItem("argument_2", argument_2);
        return mv;
    }
```

  - Si vous n'utilisez pas d'IDE, pour compiler votre classe, utilisez la commande suivante:
            
```s
    javac -parameters VotreClasse.java
```
    
  Remarque: n'oubliez pas d'ajouter en item du ModelView les attributs que vous voulez en attribut du 

### Sessions


Pour ajouter une session, on utilise les sessions d'un modelView:

```
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
```

Il est à noter que les sessions dans l'exemple sessionName valeur de présence d'authentification réussi. La session profil une valeur de paramètre pour les différents rôles
```xml
<servlet>
      <servlet-name>FrontServlet</servlet-name>
      <servlet-class>etu1748.framework.servlet.FrontServlet</servlet-class>
      <init-param>
        <param-name>sessionName</param-name>
        <param-value>isConnected</param-value>
      </init-param>
      <init-param>
        <param-name>profilName</param-name>
        <param-value>profil</param-value>
      </init-param>
    </servlet>
```

### Singletons

Pour utiliser un objet Singeton: l'objet est enregistré dans le FrontServlet, on mets une annotation @Scope("singleton")
```java
  @Scope("singleton")
public class Emp {
    private int id;
    private String nom;
```

### JSON
  
Si on veut utiliser l'application comme API, nous avons 2 méthode: dire si le ModelView veut retourner les valeurs des data:

```java
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
```

   ou retouner des objets JSON et non de ModelView:
   
```java
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
``` 
