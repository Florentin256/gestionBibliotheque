<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="entites.*,javax.naming.*,java.sql.*,javax.sql.*,java.util.*,dao.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	AuteurDAO Adao = new AuteurDAO();
	Auteur mod = null;
	try {
		mod = Adao.getAuteurWithId(Integer.valueOf(request.getParameter("id")));
	} catch (NumberFormatException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	out.println("<form method='post' action='modifAuteur'>");
	out.println("<input type='hidden' name='id' value='" + mod.getId() + "'>");
	out.println("Nom: <input type='text' name='nom' value='" + mod.getNom() + "'>");
	out.println("Prenom: <input type='text' name='prenom' value='" + mod.getPrenom() + "'>");
	out.println("<input type='submit'>");
	out.println("</form>");
	%>
</body>
</html>