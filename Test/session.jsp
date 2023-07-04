<%@page import="modele.Emp" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Set" %>
<%
    Emp emp = (Emp) request.getAttribute("emp");
    HashMap<String,Object> empSession = emp.getSession();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h2> Liste des sessions </h2>
    <% if(empSession != null){
        Set<String> keys = empSession.keySet();
            for(String key : keys){ %>
        <bold> Nom de session: </bold> <% out.print(key); %> <br>
        <bold> Valeurs: </bold> <% out.print(empSession.get(key)); %> <br><br>
        <% }
    } else { %>
            Pas de session </br>
    <% } %>
    <a href="<%=request.getContextPath()%>/index.jsp"> Index </a>
</body>
</html>