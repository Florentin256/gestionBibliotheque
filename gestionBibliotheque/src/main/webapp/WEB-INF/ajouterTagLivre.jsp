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
	LivreDAO Ldao = new LivreDAO();
	
	ArrayList<String> tags = Ldao.getLivreTagWithId(Integer.valueOf(request.getParameter("id")));
	if (tags.size() != 0) {
		for (int j=0; j<tags.size(); j++) {
			out.println(tags.get(j));
		}
	}
	
	out.println("<form method='post' action='ajoutTagLivre'>");
	out.println("<input type='hidden' name='id' value='" + request.getParameter("id") + "'>");
	out.println("<input type='text' name='newTag'>");
	out.println("<input type='submit'>");
	out.println("</form>");
	%>
</body>
</html>