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
                if (map.getMethod().equalsIgnoreCase("save")) {
                    Field[] champs = new_class.getDeclaredFields();
                    for (Field field : champs) {
                        String input = req.getParameter(field.getName());
                        Object value = Util.parseType(input, field.getType());

                        field.setAccessible(true);
                        field.set(instance, value);
                        field.setAccessible(false);
                    }
                    req.setAttribute("saved", instance);
                }
                Method method = new_class.getDeclaredMethod(map.getMethod());
                ModelView mv = (ModelView) method.invoke(instance);
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