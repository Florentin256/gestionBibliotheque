<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="entites.*,java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	Auteur mod = (Auteur)request.getAttribute("auteur");
	
	out.println("<form method='post' action='modifAuteur'>");
	out.println("<input type='hidden' name='id' value='" + mod.getId() + "'>");
	out.println("Nom: <input type='text' name='nom' value='" + mod.getNom() + "'>");
	out.println("Prenom: <input type='text' name='prenom' value='" + mod.getPrenom() + "'>");
	out.println("<input type='submit'>");
	out.println("</form>");
	%>
</body>
</html>