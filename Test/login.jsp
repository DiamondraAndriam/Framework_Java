<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <form action="<%=request.getContextPath()%>/authentification" >
        Login:<input type="text" name="login"><br>
        Mot de passe:<input type="password" name="password"><br>
        <input type="submit" value="Se connecter">
    </form>
</body>
</html>