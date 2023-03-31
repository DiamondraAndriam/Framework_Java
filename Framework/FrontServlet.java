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
        List<Class<?>> classes = util.getClasses(directory,"");
        for(int i = 0; i < classes.size(); i ++){
            Method[] methods = classes.get(i).getDeclaredMethods();
            for(Method method : methods){
                if(method.isAnnotationPresent(Urls.class)){
                    Mapping mapping = new Mapping(classes.get(i).getName(),method.getName());
                    Urls urls = (Urls) method.getAnnotation(Urls.class);
                    String url = urls.value();
                    mappingUrls.put(url,mapping);
                }
            }
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        String url = Util.getBaseURL(req.getRequestURL().toString());
        PrintWriter out = res.getWriter();
        Set<String> mappingUrlsKey = mappingUrls.keySet();
        res.setContentType("text/html;charset=UTF-8");
        out.println("<table><tr><th>Url</th><th>Class</th><th>Method</th></tr>");
        for(String key : mappingUrlsKey){
            Mapping map = (Mapping) mappingUrls.get(key);
            out.println("<tr>");
            out.println("<td>" + key + "</td><td>" + map.getClassName() + "</td><td>" + map.getMethod() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }
}