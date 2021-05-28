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
	@SuppressWarnings("unchecked")
	ArrayList<String> tags = (ArrayList<String>)request.getAttribute("tagsLivre");;
	if (tags.size() != 0) {
		for (int j=0; j<tags.size(); j++) {
			out.println(tags.get(j));
		}
	}
	%>
	
	<form method='post' action='livres'>
		<input type='hidden' name='action' value='ajoutTagLivre'>
		<input type='hidden' name='id' value='<%= request.getParameter("id") %>'>
		<input type='text' name='newTag'>
		<input type='submit'>
	</form>
	
</body>
</html>