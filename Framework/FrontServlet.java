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

    public void init() {
        mappingUrls = new HashMap<String, Mapping>();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File directory = new File(path);
        System.out.println(directory.getAbsolutePath());
        Util util = new Util();
        List<Class<?>> classes = util.getClasses(directory, "");
        for (int i = 0; i < classes.size(); i++) {
            Method[] methods = classes.get(i).getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Urls.class)) {
                    Mapping mapping = new Mapping(classes.get(i).getName(), method.getName());
                    Urls urls = (Urls) method.getAnnotation(Urls.class);
                    String url = urls.value();
                    mappingUrls.put(url, mapping);
                }
            }
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        String url = Util.getBaseURL(req.getRequestURL().toString());
        PrintWriter out = res.getWriter();
        Mapping map = (Mapping) mappingUrls.get(url);
        if (map == null) {
            out.println("Erreur 404: URL not found");
        } else {
            try {
                Class<?> new_class = Class.forName(map.getClassName());
                Object instance = new_class.newInstance();
                System.out.println(map.getMethod());
                ModelView mv = null;
                Method method = null;
                Object[] paramsValue = null;

                try {
                    method = new_class.getDeclaredMethod(map.getMethod());
                } catch (Exception e) {
                    method = Util.findMethod(map.getMethod(), new_class);
                    if (method == null) {
                        throw new Exception("Method introuvable, vérifiez le nom de la méthode");
                    }
                    Parameter[] params = method.getParameters();
                    paramsValue = new Object[params.length];
                    Class<?> paramType = null;
                    for (int i = 0; i < params.length; i++) {
                        try {
                            paramType = params[i].getType();
                            System.out.println(paramType.getName());
                            paramsValue[i] = Util.parseType(req.getParameter(params[i].getName()), paramType);
                            System.out.println(paramsValue[i]);
                        } catch (Exception e1) {
                            throw new Exception("Erreur avec le paramètre : " + params[i].getName());
                        }
                    }
                }

                if (map.getMethod().equalsIgnoreCase("save")) {
                    Field[] champs = new_class.getDeclaredFields();
                    for (Field field : champs) {
                        String input = req.getParameter(field.getName());
                        Object value = Util.parseType(input, field.getType());

                        field.setAccessible(true);
                        field.set(instance, value);
                        field.setAccessible(false);
                    }
                }
                try {
                    mv = (ModelView) method.invoke(instance);
                } catch (Exception e2) {
                    mv = (ModelView) method.invoke(instance, paramsValue);
                }

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