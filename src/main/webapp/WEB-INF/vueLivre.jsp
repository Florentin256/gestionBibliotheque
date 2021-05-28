<%@page import="beans.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Livres</h2>
	
	<!-- Liste des livres -->
	<table>
		<caption>Liste des livres</caption>
		<thead>
			<tr>
				<th id=null>Titre</th>
				<th id=null>Date de parution</th>
				<th id=null>Auteur</th>
				<th id=null>Tags</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="book" items="${livresOffset}">
				<form method='post' action='livres'>
					<input type='hidden' name='id' value='${book.getId()}'>
					<tr>
						<td>${book.getTitre()}</td>
						<td>${book.getDateParution()}</td>
						<td>${book.getAuteur().getNom()} ${book.getAuteur().getPrenom()}</td>
						<td>
							<c:forEach var="tag" items="${book.getTags()}">
								${tag}
							</c:forEach>
						</td>
						<td><input type='submit' name='action' value='Ajouter des Tags'></td>
						<td><input type='submit' name='action' value='modifier'></td>
						<td><input type='submit' name='action' value='supprimer'></td>
					</tr>
				</form>
			</c:forEach>
		</tbody>
	</table>
	
	<!-- Boutons de pagination -->
	<a href="livres?numPage=${ numPageLivres }>&action=prev">precedent</a>
	<a href="livres?numPage=${ numPageLivres }&action=next">suivant</a>
	
	<br><br>
	
	<!--  Formulaire d'ajout -->
	<form method="post" action="livres">
		<input type="hidden" name="action" value="addLivre">
		<table>
			<caption>Formulaire d'ajout</caption>
			<tr>
				<th id=null>Titre</th>
				<td><input type="text" name="titre" required></td>
			</tr>
			<tr>
				<th id=null>Date de parution</th>
				<td><input type="date" name="dateParution" required></td>
			</tr>
			<tr>
				<th id=null>Auteur</th>
				<td><input type="number" name="id" required></td>
		    </tr>
		</table>
		<input type="submit" value="Ajouter">
	</form>
</body>
</html>