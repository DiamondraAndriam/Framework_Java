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
    <h2> Test Set automatique </h2>
    <a href="<%=request.getContextPath()%>/emp-all">Liste</a><br/>
    <a href="<%=request.getContextPath()%>/emp-add">Ajouter</a><br/>
    <a href="<%=request.getContextPath()%>/file_upload.jsp">Test Employer</a><br/>
    <br>
    <h2> Test Singleton </h2>
    <a href="<%=request.getContextPath()%>/dept-view"> test singleton dept </a><br/>
    <a href="<%=request.getContextPath()%>/test-singleton"> test singleton emp</a>
    <br>
    <h2> Test Session </h2>
    <a href="<%=request.getContextPath()%>/login.jsp"> Se connecter </a><br/>
    <a href="<%=request.getContextPath()%>/profil"> Voir profil</a><br/>
    <a href="<%=request.getContextPath()%>/admin"> Voir zone admin</a><br/>
    <a href="<%=request.getContextPath()%>/info_session"> Voir données session</a><br/>
    <h2> Test JSON </h2>
    <a href="<%=request.getContextPath()%>/get_emps"> Liste employés(mv)</a><br/>
    
</body>
</html>