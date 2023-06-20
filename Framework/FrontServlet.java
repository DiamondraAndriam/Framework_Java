package etu1748.framework.servlet;

import etu1748.framework.*;
import etu1748.framework.util.*;
import etu1748.framework.annotation.*;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.lang.reflect.*;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> singletons;
    Util util = new Util();

    public void init() {
        mappingUrls = new HashMap<String, Mapping>();
        singletons = new HashMap<String, Object>();

        // obtenir le chemin absolu du répertoire
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File directory = new File(path);
        System.out.println(directory.getAbsolutePath());

        // obtenir toutes les classes
        List<Class<?>> classes = util.getClasses(directory, "");
        for (int i = 0; i < classes.size(); i++) {
            String className = classes.get(i).getName();

            // ajouter à singletons si singleton
            /*
             * if (classes.get(i).isAnnotationPresent(Scope.class)) {
             * Scope scope = (Scope) classes.get(i).getAnnotation(Scope.class);
             * if (scope.value().equalsIgnoreCase("singleton")) {
             * singletons.put(className, null);
             * }
             * }
             */

            // ajouter à mappingUrls tous les méthodes des classes avec une annotation Urls
            Method[] methods = classes.get(i).getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Urls.class)) {
                    Mapping mapping = new Mapping(className, method.getName());
                    Urls urls = (Urls) method.getAnnotation(Urls.class);
                    String url = urls.value();
                    mappingUrls.put(url, mapping);
                }
            }
        }
    }

    public void autoset(HttpServletRequest req, Object instance) throws Exception {
        System.out.println("miditra autoset");
        Class<?> new_class = instance.getClass();
        Field[] champs = new_class.getDeclaredFields();
        // reset(champs, instance);
        for (Field field : champs) {
            System.out.println("boucle");
            if (field.getType() == FileUpload.class) {
                Part file = req.getPart(field.getName());
                FileUpload value = util.getFileUpload(file);
                field.setAccessible(true);
                field.set(instance, value);
                field.setAccessible(false);
            } else {
                String input = req.getParameter(field.getName());
                if (input != null) {
                    Object value = Util.parseType(input, field.getType());
                    System.out.println(input);
                    field.setAccessible(true);
                    field.set(instance, value);
                    field.setAccessible(false);
                }
            }
        }
    }

    /*
     * public void reset(Field[] champs, Object instance) throws Exception {
     * for (Field field : champs) {
     * Object defaut = util.getDefault(field);
     * field.setAccessible(true);
     * field.set(instance, defaut);
     * field.setAccessible(false);
     * }
     * }
     */

    public Object[] setMethod(Method method, HttpServletRequest req, Parameter[] params)
            throws Exception {

        String[] paramsName = null;
        Object[] paramsValue = new Object[params.length];
        Class<?> paramType = null;
        String param = "";

        // cas 1: utilisation de paramList au niveau de la méthode
        if (method.isAnnotationPresent(ParamList.class)) {
            paramsName = method.getAnnotation(ParamList.class).value();
            for (int i = 0; i < params.length; i++) {
                try {
                    paramType = params[i].getType();
                    paramsValue[i] = Util.parseType(req.getParameter(paramsName[i]), paramType);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new Exception("Erreur avec le paramètre : " + paramsName[i]);
                }
            }

            // cas 2: utilisation de param au niveau des ar
        } else {
            for (int i = 0; i < params.length; i++) {
                try {
                    paramType = params[i].getType();
                    if (params[i].isAnnotationPresent(Param.class))
                        param = params[i].getAnnotation(Param.class).value();
                    else if (method.isAnnotationPresent(ParamList.class))
                        param = paramsName[i];
                    else
                        param = params[i].getName();
                    paramsValue[i] = Util.parseType(req.getParameter(param), paramType);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new Exception("Erreur avec le paramètre : " + params[i].getName());
                }
            }
        }
        return paramsValue;
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        // prendre l'url demandé et chercher si l'url existe
        String url = Util.getBaseURL(req.getRequestURL().toString());
        PrintWriter out = res.getWriter();
        Mapping map = (Mapping) mappingUrls.get(url);

        if (map == null) {
            out.println("Erreur 404: URL not found");
        } else {

            // url trouvé
            try {
                Class<?> new_class = Class.forName(map.getClassName());
                Object instance = new_class.newInstance();
                System.out.println(map.getMethod());

                ModelView mv = null; // retour de la fonction associé à l'url
                Method method = null;
                Parameter[] params = null;
                Object[] paramsValue = null;

                try {
                    // méthode sans paramètre
                    method = new_class.getDeclaredMethod(map.getMethod());

                    // si la requête a des paramètres
                    if (req.getParameterMap().isEmpty() == false)
                        autoset(req, instance);

                } catch (Exception e) {
                    method = Util.findMethod(map.getMethod(), new_class);
                    if (method == null)
                        throw new Exception(
                                "Méthod introuvable : " + map.getMethod() + ", vérifiez le nom de la méthode");

                    // méthode avec des paramètres
                    params = method.getParameters();
                    System.out.println("manao set parameter");
                    paramsValue = setMethod(method, req, params);
                    System.out.println(paramsValue);
                }

                try {
                    mv = (ModelView) method.invoke(instance);
                } catch (Exception e2) {
                    System.out.println(paramsValue);
                    mv = (ModelView) method.invoke(instance, paramsValue);
                }

                System.out.println("mv:" + mv);
                System.out.println("tonga eto");
                // récupération des données dans le modelView
                HashMap<String, Object> data = mv.getData();
                if (data != null) {
                    Set<String> keys = data.keySet();
                    for (String key : keys) {
                        req.setAttribute(key, data.get(key));
                    }
                }

                RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                dispat.forward(req, res);

            } catch (Exception e) {
                out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }
}