<%@page import="beans.*,java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
			<h2>Auteurs</h2>
			<table>
				<caption>Liste des Auteurs</caption>
				<thead>
					<tr>
						<th id=null>Nom</th>
						<th id=null>Prenom</th>
					</tr>
				</thead>
	<%
			@SuppressWarnings("unchecked")
			ArrayList<Auteur> listAuteursOffset = (ArrayList<Auteur>)request.getAttribute("auteursOffset");
			for (int i=0; i<listAuteursOffset.size(); i++) {
				%>
				<form method='post' action='actionAuteur'>
					<input type='hidden' name='id' value='<%=listAuteursOffset.get(i).getId()%>'>
					<tr>
						<td><%=listAuteursOffset.get(i).getNom() %></td>
						<td><%=listAuteursOffset.get(i).getPrenom() %></td>
						<td><input type='submit' name='submit' value='modifier'></td>
						<td><input type='submit' name='submit' value='supprimer'></td>
					</tr>
				</form>
				<%
			}
			int numPage = (int)request.getAttribute("numPageAuteurs");
	%>
			</table>
			<form method="post" action="auteursPrecedents">
				<input type="hidden" name="numPageAuteurs" value="<%= numPage %>">
				<input type="hidden" name="action" value="previousAuteurs">
				<input type="submit" value="precedent">
			</form>
			<form method="post" action="auteursSuivants">
				<input type="hidden" name="numPageAuteurs" value="<%= numPage %>">
				<input type="hidden" name="action" value="nextAuteurs">
				<input type="submit" value="suivant">
			</form>

			
			<p>AJOUT</p>
			<form method="post" action="ajoutAuteur">
				<input type="hidden" name="action" value="addAuteur">
				<table>
					<caption>Formulaire d'ajout</caption>
					<tr>
						<th id=null></th>
					</tr>
					<tr>
						<td>Nom</td> <td><input type="text" name="nom" required></td>
					</tr>
					<tr>
						<td>Prenom</td> <td><input type="text" name="prenom" required></td>
					</tr>
				</table>
				<input type="submit" value="Ajouter">
			</form>
</body>
</html>