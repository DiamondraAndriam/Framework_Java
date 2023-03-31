package etu1748.framework.servlet;

import etu1748.framework.*;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("tonga");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }
}