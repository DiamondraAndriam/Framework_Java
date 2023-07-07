<%@page import="modele.Emp" %>
<%
    Emp emp = (Emp) request.getAttribute("Emp");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil</title>
</head>
<body>
    <% out.print(emp.getLogin()); %><br>
    <% out.print(emp.getPassword()); %>
    <br>
    <a href="<%=request.getContextPath()%>/index.jsp"> Index </a>
</body>
</html>