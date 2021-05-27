<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="beans.*,java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	Livre mod = (Livre)request.getAttribute("livre");
	%>
	
	<form method='post' action='livres'>
		<input type='hidden' name='action' value='putLivre'>
		<input type='hidden' name='id' value='<%= mod.getId() %>'>
		Titre: <input type='text' name='titre' value='<%= mod.getTitre() %>'>
		Date de parution: <input type='date' name='dateParution' value='<%= mod.getDateParution() %>'>
		Auteur: <%= mod.getAuteur().getNom() %> <%= mod.getAuteur().getPrenom() %>
		<input type='number' name='id_auteur' required>
		<input type='submit'>
	</form>
	
</body>
</html>