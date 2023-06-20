<!DOCTYPE html> 
<html> 
<head> 
<title> Java File Upload Servlet Example </title> 
</head> 
<body>

  <form method="get" action="<%=request.getContextPath()%>/upload">
    <div> Nom </div>
    <input type="text" name="nom" id="nom"><br/>
    <div> Fichier </div>
    <input type="file" name="badge" />
    <input type="submit" value="Upload" />
  </form>
</body>
</html>