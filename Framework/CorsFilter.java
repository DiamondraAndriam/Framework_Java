package etu1748.framework.servlet;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class CorsFilter implements Filter {
    public CorsFilter(){}

    public void init(FilterConfig config){}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filter) throws IOException, ServletException {
        HttpServletRequest servletReq= (HttpServletRequest) req;
        HttpServletResponse servletRes= (HttpServletResponse) res;

        servletRes.setHeader("Access-Control-Allow-Origin", "*");
        servletRes.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        servletRes.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if(servletReq.getMethod().equals("OPTIONS")) {
            servletRes.setStatus(202);
            System.out.println("CORS OPTIONS");
        }

        filter.doFilter(servletReq, res);
    }

    public void destroy(){}
}
