<h1>Ajouter un employe</h1>
<form action="<%=request.getContextPath()%>/emp-save">
    <label for="id">Id</label><br/>
    <input type="int" name="id"><br/>
    <label for="nom">Nom</label></br>
    <input type="text" name="nom"><br/>
    <label for="date_naissance">Date de naissance</label><br/>
    <input type="date" name="date_naissance"><br/>
    <input type="submit" value="Ajouter">
</form>