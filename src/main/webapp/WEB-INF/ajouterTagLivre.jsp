<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="beans.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<c:forEach var="tag" items="${tagsLivre}">
		${tag}
	</c:forEach>
	
	<form method='post' action='livres'>
		<input type='hidden' name='action' value='ajoutTagLivre'>
		<input type='hidden' name='id' value='<%= request.getParameter("id") %>'>
		<input type='text' name='newTag'>
		<input type='submit'>
	</form>
	
</body>
</html>