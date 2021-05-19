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
	Livre mod = (Livre)request.getAttribute("livre");
	ArrayList<Auteur> listAuteurs = (ArrayList<Auteur>)request.getAttribute("auteurs");
	
	
	out.println("<form method='post' action='modifLivre'>");
	out.println("<input type='hidden' name='id' value='" + mod.getId() + "'>");
	out.println("Titre: <input type='text' name='titre' value='" + mod.getTitre() + "'>");
	out.println("Date de parution: <input type='date' name='dateParution' value='" + mod.getDateParution() + "'>");
	out.println("Auteur: " + mod.getAuteur().getNom() + " " + mod.getAuteur().getPrenom());
	out.println("<SELECT name='id_auteur'>");
	for (int i=0; i<listAuteurs.size(); i++) {
		out.println("<option value='" + listAuteurs.get(i).getId() + "'>" +
					listAuteurs.get(i).getNom() + " " + 
					listAuteurs.get(i).getPrenom() + "</option>");
	}
	out.println("</SELECT>");
	out.println("<input type='submit'>");
	out.println("</form>");
	%>
</body>
</html>