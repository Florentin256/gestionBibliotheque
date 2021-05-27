<%@page import="beans.*,java.util.*" %>
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
				<th id=null>Nom</th>
				<th id=null>Prenom</th>
			</tr>
		</thead>
		<tbody>
			<%
			@SuppressWarnings("unchecked")
			ArrayList<Auteur> listAuteursOffset = (ArrayList<Auteur>)request.getAttribute("auteursOffset");
			for (int i=0; i<listAuteursOffset.size(); i++) {
			%>
				<form method='post' action='auteurs'>
					<input type='hidden' name='id' value='<%=listAuteursOffset.get(i).getId()%>'>
					<tr>
						<td><%=listAuteursOffset.get(i).getNom() %></td>
						<td><%=listAuteursOffset.get(i).getPrenom() %></td>
						<td><input type='submit' name='action' value='modifier'></td>
						<td><input type='submit' name='action' value='supprimer'></td>
					</tr>
				</form>
			<%
			}
			int numPage = (int)request.getAttribute("numPageAuteurs");
			%>
		</tbody>
	</table>
	
	<!-- Boutons de pagination -->
	<form method="post" action="auteurs">
		<input type="hidden" name="numPageAuteurs" value="<%= numPage %>">
		<input type="submit" name="action" value="previousAuteurs">
		<input type="submit" name="action" value="nextAuteurs">
	</form>
	
	<br><br>

	<!--  Formulaire d'ajout -->
	<form method="post" action="auteurs">
		<input type="hidden" name="action" value="addAuteur">
		<table>
			<caption>Formulaire d'ajout</caption>
			<tr>
				<th id=null>Nom</th> <td><input type="text" name="nom" required></td>
			</tr>
			<tr>
				<th id=null>Prenom</th> <td><input type="text" name="prenom" required></td>
			</tr>
		</table>
		<input type="submit" value="Ajouter">
	</form>
</body>
</html>