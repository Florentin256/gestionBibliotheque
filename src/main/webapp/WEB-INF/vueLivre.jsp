<%@page import="beans.*,java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
			<h2>Livres</h2>
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
	<%
			@SuppressWarnings("unchecked")
			ArrayList<Livre> listLivresOffset = (ArrayList<Livre>)request.getAttribute("livresOffset");
			for (int i=0; i<listLivresOffset.size(); i++) {
				%>
				<form method='post' action='livres'>
					<input type='hidden' name='id' value='<%=listLivresOffset.get(i).getId() %>'>
					<tr>
						<td><%=listLivresOffset.get(i).getTitre() %></td>
						<td><%=listLivresOffset.get(i).getDateParution() %></td>
						<td><%=listLivresOffset.get(i).getAuteur().getNom() %> <%=listLivresOffset.get(i).getAuteur().getPrenom() %></td>
						<td>
							<%
							ArrayList<String> tags = (ArrayList<String>)listLivresOffset.get(i).getTags();
							for (int j=0; j<tags.size(); j++) {
								out.println(tags.get(j));
							}
							%>
						</td>
						<td><input type='submit' name='action' value='Ajouter des Tags'></td>
						<td><input type='submit' name='action' value='modifier'></td>
						<td><input type='submit' name='action' value='supprimer'></td>
					</tr>
				</form>
				<%
			}
			int numPage = (int)request.getAttribute("numPageLivres");
	%>
			</table>
			<form method="post" action="livres">
				<input type="hidden" name="numPageLivres" value="<%= numPage %>">
				<input type="hidden" name="action" value="previousLivres">
				<input type="submit" value="precedent">
			</form>
			<form method="post" action="livres">
				<input type="hidden" name="numPageLivres" value="<%= numPage %>">
				<input type="hidden" name="action" value="nextLivres">
				<input type="submit" value="suivant">
			</form>
			
			<p>AJOUT</p>
			<form method="post" action="livres">
				<input type="hidden" name="action" value="addLivre">
				<table>
					<caption>Formulaire d'ajout</caption>
					<tr>
						<th id=null></th>
					</tr>
					<tr>
						<td>Titre</td> <td><input type="text" name="titre" required></td>
					</tr>
					<tr>
						<td>Date de parution</td> <td><input type="date" name="dateParution" required></td>
					</tr>
					<tr>
						<td>Auteur</td>
						<td><input type="number" name="id" required></td>
				    </tr>
				</table>
				<input type="submit" value="Ajouter">
			</form>
</body>
</html>