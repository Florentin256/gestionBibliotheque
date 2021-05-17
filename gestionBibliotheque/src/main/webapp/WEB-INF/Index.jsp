<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="entites.*,javax.naming.*,java.sql.*,javax.sql.*,java.util.*,dao.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>table, td, th {border: 1px solid black;} table{border-collapse: collapse;}</style>
<title>Index</title>
</head>
<body>
	<h1>Gestion Bibliothèque</h1>
	<%
	AuteurDAO Adao = new AuteurDAO();	
	ArrayList<Auteur> listAuteurs = Adao.getAuteurs();
	LivreDAO Ldao = new LivreDAO();
	ArrayList<Livre> listLivres = Ldao.getLivres();
	Collections.sort(listLivres);
	%>
	
	<hr>
	
	<a href="indexAuteur">Auteurs</a>
	<a href="indexLivre">Livres</a>
	
	<hr>
	
	<%
		if (request.getAttribute("indexChoix")!=null && request.getAttribute("indexChoix").equals("indexAuteur")) {
	%>
			<h2>Auteurs</h2>
			<table>
				<thead>
					<td>Nom</td>
					<td>Prenom</td>
				</thead>
	<%
			for (int i=0; i<listAuteurs.size(); i++) {
				%>
				<form method='post' action='actionAuteur'>
					<input type='hidden' name='id' value='<%=listAuteurs.get(i).getId()%>'>
					<tr>
						<td><%=listAuteurs.get(i).getNom() %></td>
						<td><%=listAuteurs.get(i).getPrenom() %></td>
						<td><input type='submit' name='submit' value='modifier'></td>
						<td><input type='submit' name='submit' value='supprimer'></td>
					</tr>
				</form>
				<%
			}
	%>
			</table>
			
			<p>AJOUT</p>
			<form method="post" action="ajoutAuteur">
				<table>
					<tr>
						<td>Nom</td> <td><input type="text" name="nom" required></td>
					</tr>
					<tr>
						<td>Prenom</td> <td><input type="text" name="prenom" required></td>
					</tr>
				</table>
				<input type="submit" value="Ajouter">
			</form>
	<%
		} else if (request.getAttribute("indexChoix")!=null && request.getAttribute("indexChoix").equals("indexLivre")) {
	%>
			<h2>Livres</h2>
			<table>
				<thead>
					<td>Titre</td>
					<td>Date de parution</td>
					<td>Auteur</td>
					<td>Tags</td>
				</thead>
	<%
			for (int i=0; i<listLivres.size(); i++) {
				%>
				<form method='post' action='actionLivre'>
					<input type='hidden' name='id' value='<%=listLivres.get(i).getId() %>'>
					<tr>
						<td><%=listLivres.get(i).getTitre() %></td>
						<td><%=listLivres.get(i).getDateParution() %></td>
						<td><%=listLivres.get(i).getAuteur().getNom() %> <%=listLivres.get(i).getAuteur().getPrenom() %></td>
						<td>
							<%
							ArrayList<String> tags = Ldao.getLivreTagWithId(listLivres.get(i).getId());
							for (int j=0; j<tags.size(); j++) {
								out.println(tags.get(j));
							}
							%>
						</td>
						<td><input type='submit' name='submit' value='Ajouter des Tags'></td>
						<td><input type='submit' name='submit' value='modifier'></td>
						<td><input type='submit' name='submit' value='supprimer'></td>
					</tr>
				</form>
				<%
			}
	%>
			</table>
			
			<p>AJOUT</p>
			<form method="post" action="ajoutLivre">
				<table>
					<tr>
						<td>Titre</td> <td><input type="text" name="titre" required></td>
					</tr>
					<tr>
						<td>Date de parution</td> <td><input type="date" name="dateParution" required></td>
					</tr>
					<tr>
						<td>Auteur</td>
						<td>
						<SELECT name="id">
							<%
							for (int i=0; i<listAuteurs.size(); i++) {
								out.println("<option value='" + listAuteurs.get(i).getId() + "'>" +
											listAuteurs.get(i).getNom() + " " + 
											listAuteurs.get(i).getPrenom() + "</option>");
							}
							%>
				    	</SELECT>
				    	</td>
				    </tr>
				</table>
				<input type="submit" value="Ajouter">
			</form>
	<%
		}
	%>
	
</body>
</html>