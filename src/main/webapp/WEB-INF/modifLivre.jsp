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
	
	<form method='post' action='livres'>
		<input type='hidden' name='action' value='putLivre'>
		<input type='hidden' name='id' value='${livre.getId()}'>
		Titre: <input type='text' name='titre' value='${livre.getTitre()}'>
		Date de parution: <input type='date' name='dateParution' value='${livre.getDateParution()}'>
		Auteur: ${livre.getAuteur().getNom()} ${livre.getAuteur().getPrenom()}
		<input type='number' name='id_auteur' value='${livre.getAuteur().getId()}' required>
		<input type='submit'>
	</form>
	
</body>
</html>