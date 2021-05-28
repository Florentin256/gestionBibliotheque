<%@page import="beans.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Auteurs</h2>
	
	<!-- Liste des auteurs -->
	<table>
		<caption>Liste des Auteurs</caption>
		<thead>
			<tr>
				<th id=null>ID</th>
				<th id=null>Nom</th>
				<th id=null>Prenom</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="author" items="${auteursOffset}">
				<form method='post' action='auteurs'>
				<tr>
					<td><input type='hidden' name='id' value='${author.getId()}'>${author.getId()}</td>
					<td>${author.getNom()}</td>
					<td>${author.getPrenom()}</td>
					<td><input type='submit' name='action' value='modifier'></td>
					<td><input type='submit' name='action' value='supprimer'></td>
				</tr>
				</form>
			</c:forEach>
		</tbody>
	</table>
	
	<!-- Boutons de pagination -->
	<a href="auteurs?numPage=${ numPageAuteurs }&action=prev">precedent</a>
	<a href="auteurs?numPage=${ numPageAuteurs }&action=next">suivant</a>
	
	<br><br>

	<!--  Formulaire d'ajout -->
	<form method="post" action="auteurs">
		<input type="hidden" name="action" value="addAuteur">
		<table>
			<caption>Formulaire d'ajout</caption>
			<tr>
				<th id=null>Nom</th>
				<td><input type="text" name="nom" required></td>
			</tr>
			<tr>
				<th id=null>Prenom</th>
				<td><input type="text" name="prenom" required></td>
			</tr>
		</table>
		<input type="submit" value="Ajouter">
	</form>

</body>
</html>