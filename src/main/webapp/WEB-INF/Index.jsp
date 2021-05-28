<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="beans.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<style>table, td, th {border: 1px solid black;} table{border-collapse: collapse;}</style>
<title>Index</title>
</head>
<body>
	<h1>Gestion Bibliothèque</h1><a href="login?action=deconnexion">Deconnexion</a>
	<hr>
	
	<a href="auteurs">Auteurs</a>
	<a href="livres">Livres</a>
	
	<hr>
	
	<c:choose>
		<c:when test='${ not empty indexChoix && indexChoix == "indexAuteur" }'>
			<%@include file="vueAuteur.jsp" %>
		</c:when>
		<c:when test='${ not empty indexChoix && indexChoix == "indexLivre" }'>
			<%@include file="vueLivre.jsp" %>
		</c:when>
	</c:choose>
	
</body>
</html>