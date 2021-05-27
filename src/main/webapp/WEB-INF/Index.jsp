<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="beans.*,java.util.*" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<style>table, td, th {border: 1px solid black;} table{border-collapse: collapse;}</style>
<title>Index</title>
</head>
<body>
	<h1>Gestion Bibliothèque</h1><a href="deconnexion">Deconnexion</a>
	<hr>
	
	<a href="auteurs">Auteurs</a>
	<a href="livres">Livres</a>
	
	<hr>
	
	<%
		if (request.getAttribute("indexChoix")!=null && request.getAttribute("indexChoix").equals("indexAuteur")) {
	%>
			<%@include file="vueAuteur.jsp" %>
	<%
		} else if (request.getAttribute("indexChoix")!=null && request.getAttribute("indexChoix").equals("indexLivre")) {
	%>
			<%@include file="vueLivre.jsp" %>
	<%
		}
	%>
	
</body>
</html>