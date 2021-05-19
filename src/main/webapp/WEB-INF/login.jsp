<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<h1>Connexion</h1>
	
	<hr>
	<form method="post" action="login">
		<table>
			<tr>
				<td>login</td>
				<td><input type="text" name="login"></td>
			</tr>
			<tr>
				<td>password</td>
				<td><input type="password" name="password"></td>
			</tr>
		</table>
		<input type="submit" value="Se connecter">
	</form>
</body>
</html>