<% out.println(request.getAttribute("singleton")); %>
<%
    int id = (int) request.getAttribute("id");
    String nom = (String) request.getAttribute("nom");
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
    Id : <%=id%> <br/>
    Nom : <%=nom%>
    <br>
    <a href="<%=request.getContextPath()%>/index.jsp"> Index </a>

</body>
</html>