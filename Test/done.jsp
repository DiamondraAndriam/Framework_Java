<%@page import="modele.Emp" %>
<%
    Emp emp = (Emp) request.getAttribute("emp");
%>
<% out.println(request.getAttribute("singleton")); %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h2>Téléchargement réussi</h2>
    <p> Taille du fichier <% emp.getBadge().getFile().length %> </p>
<br>
<a href="<%=request.getContextPath()%>/index.jsp"> Index </a>

</body>
</html>