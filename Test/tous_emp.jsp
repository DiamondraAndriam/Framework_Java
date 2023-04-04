<%@page import="java.util.List" %>
<%@page import="modele.Emp" %>
<%
    List<Emp> liste = (List<Emp>) request.getAttribute("liste");
%>
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
    <center>
        <h1>Liste des employés</h1>
        <br/>
        <table>
            <tr>
                <th width="100px">Id</th>
                <th width="200px">Nom</th>
            </tr>
            <% for(Emp emp : liste){ %>
            <tr>
                <td><% out.println(emp.getId()); %></td>
                <td><% out.println(emp.getNom()); %></td>
            </tr>
            <% } %>
        </table>
    </center>
</body>
</html>