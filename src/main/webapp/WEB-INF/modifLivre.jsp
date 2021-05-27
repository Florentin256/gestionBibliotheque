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
	
	out.println("<form method='post' action='livres'>");
	out.println("<input type='hidden' name='id' value='" + mod.getId() + "'>");
	out.println("<input type='hidden' name='action' value='putLivre'>");
	out.println("Titre: <input type='text' name='titre' value='" + mod.getTitre() + "'>");
	out.println("Date de parution: <input type='date' name='dateParution' value='" + mod.getDateParution() + "'>");
	out.println("Auteur: " + mod.getAuteur().getNom() + " " + mod.getAuteur().getPrenom());
	out.println("<input type='number' name='id_auteur' required>");
	out.println("<input type='submit'>");
	out.println("</form>");
	%>
</body>
</html>