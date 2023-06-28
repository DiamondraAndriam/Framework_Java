<%@page import="modele.Emp" %>
<%
    Emp emp = (Emp) request.getAttribute("employe");
%>
<% out.println(request.getAttribute("singleton")); %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste</title>
</head>
<body>
    <h1>Employé sauvegardé</h1>
    <p>Id: <% out.print(emp.getId()); %></p>
    <p>Nom: <% out.print(emp.getNom()); %></p>
    <p>Date de naissance: <% out.print(emp.getDate_naissance()); %></p>
</body>