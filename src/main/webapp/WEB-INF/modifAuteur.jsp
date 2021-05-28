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

	<form method='post' action='auteurs'>
		<input type='hidden' name='action' value='putAuteur'>
		<input type='hidden' name='id' value='${ auteur.getId() }'>
		Nom: <input type='text' name='nom' value='${ auteur.getNom() }'>
		Prenom: <input type='text' name='prenom' value='${ auteur.getPrenom() }'>
		<input type='submit'>
	</form>

</body>
</html>