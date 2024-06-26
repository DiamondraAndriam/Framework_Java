package etu1748.framework.servlet;

import etu1748.framework.*;
import etu1748.framework.util.*;
import etu1748.framework.annotation.*;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;

import java.util.*;
import java.lang.reflect.*;

@WebServlet
@MultipartConfig
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> singletons;
    String session_connection = null;
    String session_profil = null;
    Util util = new Util();
    int appel_singleton = 0;

    // valeur initiale
    public void init() throws ServletException {
        mappingUrls = new HashMap<String, Mapping>();
        singletons = new HashMap<String, Object>();

        // nom des sessions
        session_connection = this.getInitParameter("sessionName");
        session_profil = this.getInitParameter("profilName");

        // obtenir le chemin absolu du répertoire
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File directory = new File(path);
        System.out.println(directory.getAbsolutePath());

        // obtenir toutes les classes
        List<Class<?>> classes = util.getClasses(directory, "");
        for (int i = 0; i < classes.size(); i++) {
            String className = classes.get(i).getName();

            // ajouter à singletons si singleton
            if (classes.get(i).isAnnotationPresent(Scope.class)) {
                Scope scope = (Scope) classes.get(i).getAnnotation(Scope.class);
                if (scope.value().equalsIgnoreCase("singleton")) {
                    singletons.put(className, null);
                }
            }

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

    // manao set ny objet en instance
    public void autoset(HttpServletRequest req, Object instance, String requestMethod) throws Exception {
        System.out.println("miditra autoset");
        Class<?> new_class = instance.getClass();
        Field[] champs = new_class.getDeclaredFields();
        // reset(champs, instance);
        for (Field field : champs) {
            System.out.println("boucle");
            if (field.getType() == FileUpload.class) {
                String contentType = req.getContentType();
                if (contentType != null && contentType.startsWith("multipart/form-data")
                        && requestMethod.equalsIgnoreCase("POST")) {
                    try {
                        Part file = req.getPart(field.getName());
                        FileUpload value = util.getFileUpload(file);
                        System.out.println("mivoaka ny fileUpload");
                        field.setAccessible(true);
                        field.set(instance, value);
                        field.setAccessible(false);
                    } catch (Exception e) {
                        System.out.println("misy olana: ");
                        e.printStackTrace();
                    }
                }
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

    // réinitialiser par singeton
    public void reset(Field[] champs, Object instance) throws Exception {
        for (Field field : champs) {
            Object defaut = util.getDefault(field.getType());
            field.setAccessible(true);
            field.set(instance, defaut);
            field.setAccessible(false);
        }
    }

    // set tous les attributs settables
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
                    if (method.isAnnotationPresent(JSON.class)) {
                        // maka an'ilay parametre
                    } else
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
                    if (method.isAnnotationPresent(JSON.class)) {
                        // maka an'ilay parametre
                    } else
                        paramsValue[i] = Util.parseType(req.getParameter(param), paramType);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new Exception("Erreur avec le paramètre : " + params[i].getName());
                }
            }
        }
        return paramsValue;
    }

    // main process
    protected void processRequest(HttpServletRequest req, HttpServletResponse res, String requestMethod)
            throws IOException, ServletException {

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(202);
        } else {

        }

        // prendre l'url demandé et chercher si l'url existe
        String url = Util.getBaseURL(req.getRequestURL().toString());
        Mapping map = (Mapping) mappingUrls.get(url);

        if (map == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("Ressource not found");
        } else {
            action(req, res, requestMethod, map);
        }
    }

    public void action(HttpServletRequest req, HttpServletResponse res, String requestMethod, Mapping map)
            throws IOException, ServletException {
        System.out.println("Mahita url");
        System.out.println("Classe: " + map.getClassName() + "\nMethode:" + map.getMethod() + "\n");
        // url trouvé
        PrintWriter out = res.getWriter();
        HttpSession httpSession = req.getSession();
        try {
            Object instance = null; // instance d'objet relié à l'url appelé
            Object returnObj = null; // retour de la fonction associé à l'url
            ModelView mv = null; // retour si ModelView
            Method method = null;
            Parameter[] params = null;
            Object[] paramsValue = null;
            Class<?> new_class = Class.forName(map.getClassName());

            // instanciation classe sauf singleton
            if (singletons.containsKey(map.getClassName())) {
                instance = singletons.get(map.getClassName());
                if (instance == null) {
                    instance = new_class.newInstance();
                }
                appel_singleton++;
            } else {
                instance = new_class.newInstance();
                appel_singleton = 0;
            }
            // System.out.println(map.getMethod());
            req.setAttribute("singleton", appel_singleton);

            // instanciation de la méthode
            try {
                // méthode sans paramètre
                method = new_class.getDeclaredMethod(map.getMethod());
                // si la requête a des paramètres
                if (req.getParameterMap().isEmpty() == false && method.isAnnotationPresent(JSON.class) == false) {
                    autoset(req, instance, requestMethod);
                }
                if (method.isAnnotationPresent(Paginate.class)) {
                    System.out.println("miditra pagination");
                    if (instance.getClass().getDeclaredField("pagination") != null) {
                        Field paginationField = instance.getClass().getDeclaredField("pagination");
                        if (paginationField.getType() == Pagination.class) {
                            Pagination pages = (Pagination) Util.getJSONContent(req, Pagination.class);
                            paginationField.setAccessible(true);
                            paginationField.set(instance, pages);
                            paginationField.setAccessible(false);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Miditra exception");
                method = Util.findMethod(map.getMethod(), new_class);
                if (method == null)
                    throw new Exception(
                            "Méthod introuvable : " + map.getMethod() + ", vérifiez le nom de la méthode");

                // méthode avec des paramètres
                params = method.getParameters();
                // System.out.println("manao set parameter");

                if (params.length == 1 && new_class.isAnnotationPresent(MVCController.class)
                        && method.isAnnotationPresent(JSON.class)) {
                    if (!params[0].getType().equals(new_class.getDeclaredFields()[0].getType())) {
                        throw new Exception("Erreur de création d'objet pour la classe : " + new_class.getName());
                    }
                    paramsValue = new Object[1];
                    paramsValue[0] = Util.getJSONContent(req, params[0].getType());
                    System.out.println(paramsValue[0]);
                } else {
                    paramsValue = setMethod(method, req, params);
                }
                // System.out.println(paramsValue);
            }

            // test si besoin d'authentification
            if (method.isAnnotationPresent(Auth.class)) {
                Auth auth = method.getAnnotation(Auth.class);
                if (!method.isAnnotationPresent(JSON.class)) {
                    Object connected = httpSession.getAttribute(session_connection);
                    System.out.println(session_connection);
                    if (connected == null) {
                        throw new NotAuthorizedException("Methode non authorisé. Se connecter");
                    }
                    if (auth.value().equals("") == false) {
                        Object profil = httpSession.getAttribute(session_profil);
                        if (profil == null) {
                            throw new NotAuthorizedException("Methode non authorisé. Acces refusé");
                        }
                        System.out.println(session_profil);
                    }
                } else {

                    try {
                        // check token si existe
                        String token = Util.getToken(req);
                        List<String> tokenInConfig = Util.getConfigToken();
                        boolean contains = false;
                        if (tokenInConfig == null || token == null)
                            throw new NotAuthorizedException("Methode non authorisé. Se connecter");
                        for (String string : tokenInConfig) {
                            if (token.equals(string)) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains)
                            throw new NotAuthorizedException("Methode non authorisé. Se connecter");
                        // check token si correspond
                    } catch (Exception e) {
                        // mi throws non autorisé
                        throw e;
                    }
                }
            }

            // invocation de la méthode
            try {
                returnObj = method.invoke(instance);
            } catch (Exception e2) {
                // System.out.println(paramsValue);
                returnObj = method.invoke(instance, paramsValue);
            }
            // System.out.println("mv:" + mv);
            // System.out.println("tonga eto");

            // get session si annoté session
            if (method.isAnnotationPresent(Session.class)) {
                Method method_session = new_class.getMethod("addSession", String.class, Object.class);
                method_session.setAccessible(true);
                for (Enumeration<String> e = httpSession.getAttributeNames(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();
                    method_session.invoke(instance, key, httpSession.getAttribute(key));
                }
                method_session.setAccessible(false);
            }

            // type de retour
            if (returnObj instanceof ModelView) {

                mv = (ModelView) returnObj;
                // set session
                HashMap<String, Object> mvSession = mv.getSession();
                if (mvSession != null && mvSession.isEmpty() == false) {
                    System.out.println("miditra");

                    Set<String> keys = mvSession.keySet();
                    for (String key : keys) {
                        httpSession.setAttribute(key, mvSession.get(key));
                    }
                } else {
                    if (method.getName().equalsIgnoreCase("authentificate")) {
                        throw new Exception("Authentification failed");
                        // retourne un message d'erreur dans le view suivant si authentification échoué
                    }
                }

                // si la méthode expire la session
                if (mv.isInvalidateSession() == true) {
                    httpSession.invalidate();
                }

                // récupération des données dans le modelView
                HashMap<String, Object> data = mv.getData();

                // si modelview avec retour JSON
                if (mv.isJSON()) {
                    res.setContentType("application/json");
                    String datum = util.toJson(data);
                    out = res.getWriter();
                    out.println(datum);
                } else {
                    if (data != null) {
                        Set<String> keys = data.keySet();
                        for (String key : keys) {
                            req.setAttribute(key, data.get(key));
                        }
                    }
                    // req.setAttribute("singleton", appel_singleton);
                    RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                    dispat.forward(req, res);
                }
            } else {
                // check si API REST
                if (method.isAnnotationPresent(JSON.class)) {
                    if (method.getAnnotation(JSON.class).value() != null) {
                        if (method.getAnnotation(JSON.class).value().equalsIgnoreCase(requestMethod)) {
                            res.setContentType("application/json");
                            if (returnObj instanceof ResponseAPI && returnObj != null) {
                                if (((ResponseAPI) returnObj).getError() == null)
                                    res.setStatus(HttpServletResponse.SC_OK);
                                else {
                                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    ((ResponseAPI) returnObj).setStatus(500);
                                }
                            } else {
                                res.setStatus(HttpServletResponse.SC_OK);
                            }
                            String datum = util.toJson(returnObj);
                            out = res.getWriter();
                            out.println(datum);
                        } else {
                            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        }
                    } else {
                        res.setStatus(HttpServletResponse.SC_OK);
                        if (returnObj != null) {
                            res.setContentType("application/json");
                            String datum = util.toJson(returnObj);
                            out = res.getWriter();
                            out.println(datum);
                        }
                    }
                } else
                    res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (NotAuthorizedException e) {
            System.out.println("Exception");
            e.printStackTrace(System.out);
            res.setContentType("application/json");
            out = res.getWriter();
            out.println("{\"erreur\":\"" + e.getMessage() + "\",\"status\":401}");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace(System.out);
            res.setContentType("application/json");
            out = res.getWriter();
            out.println("{\"erreur\":\"" + e.getMessage() + "\",\"status\":500}");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res, "GET");
    }

    protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res, "POST");
    }

    protected void doPut(final HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res, "PUT");
    }

    protected void doDelete(final HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        processRequest(req, res, "DELETE");
    }
}