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
    <a href="<%=request.getContextPath()%>/emp-all">Liste - sprint 5, 6 & 8 </a><br/>
    <a href="<%=request.getContextPath()%>/emp-add">Ajouter - sprint 7 </a><br/>
    <a href="<%=request.getContextPath()%>/file_upload.jsp">Test Upload fichier - sprint 9</a><br/>
    <br>
    <h2> Test Singleton </h2>
    <a href="<%=request.getContextPath()%>/dept-view"> test singleton dept - sprint 10 </a><br/>
    <a href="<%=request.getContextPath()%>/test-singleton"> test singleton emp - sprint 10</a>
    <br>
    <h2> Test Session </h2>
    <a href="<%=request.getContextPath()%>/login.jsp"> Se connecter - sprint 11 </a><br/>
    <a href="<%=request.getContextPath()%>/profil"> Voir profil - sprint 11 </a><br/>
    <a href="<%=request.getContextPath()%>/admin"> Voir zone admin - sprint 11 </a><br/>
    <a href="<%=request.getContextPath()%>/info_session"> Voir données session - sprint 12</a><br/>
    <a href="<%=request.getContextPath()%>/remove_session"> Supprimer session - sprint 15 </a><br/>

    <h2> Test JSON </h2>
    <a href="<%=request.getContextPath()%>/get_emps"> Liste employés(mv) - sprint 13 </a><br/>
    <a href="<%=request.getContextPath()%>/all_emps"> Liste employés(Emp) - sprint 14 </a><br/>
    
    
</body>
</html>