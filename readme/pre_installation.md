## A propos du framework 

### Prérequis

- Le fichier framework-jdk8-tomcat8.jar
- Environnement Java JDK8 ou OpenJDK8
- Serveur TomCat 8.x.x ou GlassFish 4.x.x

### Installation et configuration

- Ajoutez le fichier framework-jdk8-tomcat8.jar dans lib de WEB-INF de votre projet

- Veuillez configurer votre fichier web.xml dans WEB-INF en ajoutant le code suivant:

`<?xml version="1.0" encoding="UTF-8"?>

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

</web-app>`

    -  Si vous n'arrivez pas à compiler vos classes, ajoutez framework.jar à votre classpath.
